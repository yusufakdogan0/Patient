package com.akdogan.app.User.Repo;

import com.akdogan.app.User.Models.Laborant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaborantRepo extends JpaRepository<Laborant,Long> {


}
