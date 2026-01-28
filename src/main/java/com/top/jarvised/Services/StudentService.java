package com.top.jarvised.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.top.jarvised.SchoolContext;
import com.top.jarvised.DTOs.StudentResponse;
import com.top.jarvised.Entities.PointsSystem;
import com.top.jarvised.Entities.SchoolYearSettings;
import com.top.jarvised.Entities.Student;
import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Repositories.PointsSystemRepository;
import com.top.jarvised.Repositories.SchoolYearSettingsRepository;
import com.top.jarvised.Repositories.StudentRepository;
import com.top.jarvised.Repositories.UserAccountRepository;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    private SchoolYearSettingsRepository schoolYearSettingsRepository;
    private SchoolYearSettingsService schoolYearSettingsService;
    private PointsSystemRepository pointsSystemRepository;
    private UserAccountRepository userAccountRepository;

    @Autowired
    public StudentService(
            StudentRepository studentRepository,
            SchoolYearSettingsRepository schoolYearSettingsRepository,
            SchoolYearSettingsService schoolYearSettingsService,
            PointsSystemRepository pointsSystemRepository,
            UserAccountRepository userAccountRepository) {
        this.studentRepository = studentRepository;
        this.schoolYearSettingsRepository = schoolYearSettingsRepository;
        this.schoolYearSettingsService = schoolYearSettingsService;
        this.pointsSystemRepository = pointsSystemRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<StudentResponse> getAllStudents(Long schoolId, Long userAccountId) {
        System.out.println("[DEBUG] StudentService.getAllStudents - SchoolContext: " + SchoolContext.getSchool());
        List<Student> students = studentRepository.findAll();
        System.out.println("[DEBUG] StudentService.getAllStudents - found " + students.size() + " raw students");
        Integer studentPoints = calculateStudentPoints(schoolId, userAccountId);

        return students.stream()
            .map(student -> new StudentResponse(student, studentPoints))
            .collect(Collectors.toList());
    }

    /**
     * Calculates student points based on valid school days and daily increase.
     * Formula: (number of valid school days from start date to today) * dailyIncrease
     */
    private Integer calculateStudentPoints(Long schoolId, Long userAccountId) {
        // Get the active school year settings (tenant DB)
        SchoolYearSettings settings = schoolYearSettingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElse(null);

        if (settings == null || settings.getStartDate() == null) {
            return 0;
        }

        // Get the points system for dailyIncrease (master DB)
        SchoolContext.clear();
        PointsSystem pointsSystem = pointsSystemRepository.findByUserAccountId(userAccountId)
            .orElse(null);
        // Restore tenant context
        SchoolContext.setSchool(schoolId.toString());

        if (pointsSystem == null || pointsSystem.getDailyIncrease() == null || pointsSystem.getDailyIncrease() == 0) {
            return 0;
        }

        // Count valid school days from start date to today
        LocalDate startDate = settings.getStartDate();
        LocalDate today = LocalDate.now();

        // Don't count days if start date is in the future
        if (startDate.isAfter(today)) {
            return 0;
        }

        int validSchoolDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(today)) {
            if (schoolYearSettingsService.isSchoolDay(schoolId, currentDate)) {
                validSchoolDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return validSchoolDays * pointsSystem.getDailyIncrease();
    }

    public List<Student> getAllStudentsRaw() {
        return studentRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Student createStudent(Student student) {
        System.out.println("[DEBUG] StudentService.createStudent - SchoolContext: " + SchoolContext.getSchool());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    /**
     * Get user account ID by email from master DB.
     * This runs in its own transaction to isolate the master DB query.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long getUserAccountIdByEmail(String email) {
        SchoolContext.clear(); // Ensure we query master DB
        System.out.println("[DEBUG] StudentService.getUserAccountIdByEmail - SchoolContext: " + SchoolContext.getSchool());
        UserAccount user = userAccountRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
