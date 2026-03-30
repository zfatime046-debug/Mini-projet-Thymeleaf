package com.association.gestiondons.controller;

import com.association.gestiondons.dto.request.CampagneRequest;
import com.association.gestiondons.dto.request.DonRequest;
import com.association.gestiondons.dto.request.DonateurRequest;
import com.association.gestiondons.entity.MoyenPaiement;
import com.association.gestiondons.entity.StatutCampagne;
import com.association.gestiondons.entity.TypeDonateur;
import com.association.gestiondons.service.CampagneService;
import com.association.gestiondons.service.DonService;
import com.association.gestiondons.service.DonateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final DonateurService donateurService;
    private final DonService      donService;
    private final CampagneService campagneService;

    // ── Dashboard ────────────────────────────────────────────────
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("statistiques", donService.getStatistiques());
        model.addAttribute("campagnes",    campagneService.findAll());
        model.addAttribute("dons",         donService.findAll());
        return "index";
    }

    // ── Donateurs ────────────────────────────────────────────────
    @GetMapping("/donateurs")
    public String getDonateurs(Model model) {
        model.addAttribute("donateurs", donateurService.findAll());
        return "donateurs";
    }

    @PostMapping("/donateurs")
    public String addDonateur(
            @RequestParam String nom,
            @RequestParam String email,
            @RequestParam(defaultValue = "PARTICULIER") String type) {
        DonateurRequest request = new DonateurRequest();
        request.setNom(nom);
        request.setEmail(email);
        request.setType(TypeDonateur.valueOf(type));
        donateurService.create(request);
        return "redirect:/donateurs";
    }

    @PostMapping("/donateurs/edit/{id}")
    public String editDonateur(
            @PathVariable Long id,
            @RequestParam String nom,
            @RequestParam String email,
            @RequestParam(defaultValue = "PARTICULIER") String type) {
        DonateurRequest request = new DonateurRequest();
        request.setNom(nom);
        request.setEmail(email);
        request.setType(TypeDonateur.valueOf(type));
        donateurService.update(id, request);
        return "redirect:/donateurs";
    }

    @PostMapping("/donateurs/delete/{id}")
    public String deleteDonateur(@PathVariable Long id) {
        donateurService.delete(id);
        return "redirect:/donateurs";
    }

    // ── Dons ─────────────────────────────────────────────────────
    @GetMapping("/dons")
    public String getDons(Model model) {
        model.addAttribute("dons",      donService.findAll());
        model.addAttribute("campagnes", campagneService.findAll());
        model.addAttribute("donateurs", donateurService.findAll());
        return "dons";
    }

    @PostMapping("/dons")
    public String addDon(
            @RequestParam BigDecimal montant,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDon,
            @RequestParam String moyen,
            @RequestParam Long campagneId,
            @RequestParam Long donateurId) {
        DonRequest request = new DonRequest();
        request.setMontant(montant);
        request.setDateDon(dateDon);
        request.setMoyen(MoyenPaiement.valueOf(moyen));
        request.setCampagneId(campagneId);
        request.setDonateurId(donateurId);
        donService.create(request);
        return "redirect:/dons";
    }

    @PostMapping("/dons/delete/{id}")
    public String deleteDon(@PathVariable Long id) {
        donService.delete(id);
        return "redirect:/dons";
    }

    // ── Campagnes ─────────────────────────────────────────────────
    @GetMapping("/campagnes")
    public String getCampagnes(Model model) {
        model.addAttribute("campagnes", campagneService.findAll());
        return "campagnes";
    }

    @PostMapping("/campagnes")
    public String addCampagne(
            @RequestParam String titre,
            @RequestParam BigDecimal objectif,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(defaultValue = "ACTIVE") String statut) {
        CampagneRequest request = new CampagneRequest();
        request.setTitre(titre);
        request.setObjectif(objectif);
        request.setDebut(debut);
        request.setFin(fin);
        request.setStatut(StatutCampagne.valueOf(statut));
        campagneService.create(request);
        return "redirect:/campagnes";
    }

    @PostMapping("/campagnes/edit/{id}")
    public String editCampagne(
            @PathVariable Long id,
            @RequestParam String titre,
            @RequestParam BigDecimal objectif,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(defaultValue = "ACTIVE") String statut) {
        CampagneRequest request = new CampagneRequest();
        request.setTitre(titre);
        request.setObjectif(objectif);
        request.setDebut(debut);
        request.setFin(fin);
        request.setStatut(StatutCampagne.valueOf(statut));
        campagneService.update(id, request);
        return "redirect:/campagnes";
    }

    @PostMapping("/campagnes/delete/{id}")
    public String deleteCampagne(@PathVariable Long id) {
        campagneService.delete(id);
        return "redirect:/campagnes";
    }
}
