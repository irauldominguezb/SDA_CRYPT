package com.movies.movies.modules.gender.controller;

import com.movies.movies.modules.gender.model.Gender;
import com.movies.movies.modules.gender.model.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenderService {

    @Autowired
    private GenderRepository repository;

    public List<Gender> genders(){ return repository.findAll();}
}
