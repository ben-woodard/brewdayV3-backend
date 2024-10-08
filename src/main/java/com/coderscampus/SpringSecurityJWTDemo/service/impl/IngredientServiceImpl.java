package com.coderscampus.SpringSecurityJWTDemo.service.impl;

import com.coderscampus.SpringSecurityJWTDemo.domain.Company;
import com.coderscampus.SpringSecurityJWTDemo.domain.Ingredient;
import com.coderscampus.SpringSecurityJWTDemo.domain.User;
import com.coderscampus.SpringSecurityJWTDemo.dto.IngredientDto;
import com.coderscampus.SpringSecurityJWTDemo.exceptions.BadRequestException;
import com.coderscampus.SpringSecurityJWTDemo.exceptions.NotFoundException;
import com.coderscampus.SpringSecurityJWTDemo.mappers.IngredientMapper;
import com.coderscampus.SpringSecurityJWTDemo.repository.IngredientRepository;
import com.coderscampus.SpringSecurityJWTDemo.service.CompanyService;
import com.coderscampus.SpringSecurityJWTDemo.service.IngredientService;
import com.coderscampus.SpringSecurityJWTDemo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class IngredientServiceImpl  implements IngredientService{

    private final IngredientMapper ingredientMapper;
    private final CompanyService companyService;
//    private final UserService userService;
    private final IngredientRepository ingredientRepo;

    @Override
    public List<IngredientDto> findAllByCompany(Long companyId) {
        Company company =  companyService.findById(companyId);
        if(company == null) {
            throw new NotFoundException("There is no company with this id");
        }
        return ingredientMapper.entityListToDtoList(company.getIngredients());
    }

    @Override
    public IngredientDto saveIngredient(IngredientDto ingredientDto, Long companyId) {
        Ingredient ingredient = ingredientMapper.dtoToEntity(ingredientDto);
        Company company = companyService.findById(companyId);
        if(company == null) {
            throw new NotFoundException("Could not find a user with the provided id to add this ingredient to.");
        }
        ingredient.setCompany(company);
        company.getIngredients().add(ingredient);
        return ingredientMapper.entityToDto(ingredientRepo.save(ingredient));

    }
//
//    @Override
//    public List<IngredientDto> findAllByUser(Integer userId) {
//        User user = userService.findById(userId);
//        if(user == null) {
//            throw new NotFoundException("Could not find a user with the provided id.");
//        }
//        List<Ingredient> ingredients = ingredientRepo.findAllByUser(user);
//        ingredients.sort(Comparator.comparing(Ingredient::getIngredientName));
//        return ingredientMapper.entityListToDtoList(ingredients);
//    }
//
    @Override
    public IngredientDto updateIngredient(Long ingredientId, IngredientDto ingredientDto) {
        Ingredient dbIngredient = ingredientRepo.findById(ingredientId).orElse(null);
        if(dbIngredient == null) {
            throw new NotFoundException("Could not find an ingredient with the provided id to update");
        }
        Ingredient ingredient = ingredientMapper.dtoToEntity(ingredientDto);
        ingredient.setCompany(dbIngredient.getCompany());
        return ingredientMapper.entityToDto(ingredientRepo.saveAndFlush(ingredient));
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteIngredient(Long ingredientId) {
        Ingredient ingredient = checkIngredientNull(ingredientId);
        if (ingredient == null) {
            throw new NotFoundException("Could not find an ingredient with the provided id");
        }
        ingredient.getCompany().getIngredients().remove(ingredient);
        ingredient.setCompany(null);
        ingredient.getOrders().clear();
        ingredient.getRecipes().clear();
        ingredientRepo.delete(ingredient);


        if (checkIngredientNull(ingredientId) == null) {
            return ResponseEntity.ok().body("{\"message\": \"Item successfully deleted\"}");
        } else {
            throw new BadRequestException("Failed to delete the inventory item");
        }
    }

    public Ingredient checkIngredientNull(Long ingredientId) {
        Ingredient ingredient = ingredientRepo.findById(ingredientId).orElse(null);
        if(ingredient != null) {
           return ingredient;
        }
        return null;
    }

}
