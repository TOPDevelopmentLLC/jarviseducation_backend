package com.top.jarvised.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.top.jarvised.Entities.ClassCatalogueItem;

public interface ClassCatalogueRepository extends JpaRepository<ClassCatalogueItem, Long> {

    //List<ClassCatalogueItem> findAllByTeacherId();

}