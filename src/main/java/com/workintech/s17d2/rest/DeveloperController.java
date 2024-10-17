package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers = new HashMap<>();
    private Taxable developerTax;

    @Autowired
    public DeveloperController(Taxable developerTax) {
        this.developerTax = developerTax;
    }

    @PostConstruct
    public void init() {
        developers.put(1, new Developer(1, "Alice", 1000, Experience.JUNIOR));
        developers.put(2, new Developer(2, "Bob", 2000, Experience.MID));
        developers.put(3, new Developer(3, "Charlie", 3000, Experience.SENIOR));
        System.out.println("Developers initialized: " + developers);
    }

    @GetMapping
    public List<Developer> getDevelopers(){
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDevelopers(@PathVariable Integer id){
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer dev){
        double taxRate;
        Developer developer;
        switch (dev.getExperience()){
            case JUNIOR :
                taxRate = developerTax.getSimpleTaxRate();
                developer = new JuniorDeveloper();
                break;
            case MID :
                taxRate = developerTax.getMiddleTaxRate();
                developer = new JuniorDeveloper();
                break;
            case SENIOR :
                taxRate = developerTax.getUpperTaxRate();
                developer = new JuniorDeveloper();
                break;
            default:
                System.out.println("Incorrect experiance");
                return new Developer();
        }
        double income = developer.getSalary() - dev.getSalary() * taxRate;
        developer.setSalary(income);
        developer.setId(dev.getId());
        developer.setName(dev.getName());
        developers.put(developer.getId(),developer);
        return developer;
    }

    @PutMapping("/{id}")
    public void editDeveloper(@PathVariable Integer id,@RequestBody Developer developer){
        if(developers.containsKey(id)){
            developers.put(developer.getId(),developer);
        }else System.out.println("no Developer with that id");
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable Integer id){
        if (developers.containsKey(id)){
            developers.remove(id);
        }else System.out.println("id ile kimse yok");
    }

}
