package com.top.jarvised;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.top.jarvised.Repositories.AdministratorRepository;
import com.top.jarvised.Repositories.ClassCatalogueRepository;
import com.top.jarvised.Repositories.StudentRepository;
import com.top.jarvised.Repositories.TeacherRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JarvisEdBackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private StudentRepository studentRepository;
	private void setUpTestStudentData() {

	}

	@Autowired
	private TeacherRepository teacherRepository;
	private void setUpTestTeacherData() {

	}

	@Autowired 
	private AdministratorRepository administratorRepository;
	private void setUpTestAdministratorData() {

	}

	private void setUpTestParentData() {

	}

	@Autowired
	private ClassCatalogueRepository classCatalogueRepository;
	private void setUpTestClassCatalogueData() {

	}

}
