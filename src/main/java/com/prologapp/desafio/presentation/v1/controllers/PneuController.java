package com.prologapp.desafio.presentation.v1.controllers;

import com.prologapp.desafio.application.dtos.PneuRequestDTO;
import com.prologapp.desafio.application.dtos.VincularPneuDTO;
import com.prologapp.desafio.application.services.IPneuService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/pneus")
@Validated
public class PneuController {

    private IPneuService pneuService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void registrarPneu(@Valid @RequestBody PneuRequestDTO pneuRequest) {
        pneuService.registrarPneu(pneuRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/vinculacao")
    public void vincularPneuVeiculo(@Valid @RequestBody VincularPneuDTO vincularPneu) {
        pneuService.vincularPneuVeiculo(vincularPneu);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/desvinculacao/{numero-fogo}")
    public void desvincularPneuVeiculo(@PathVariable(value = "numero-fogo") Long numeroFogo) {
        pneuService.desvincularPneuVeiculo(numeroFogo);
    }
}
