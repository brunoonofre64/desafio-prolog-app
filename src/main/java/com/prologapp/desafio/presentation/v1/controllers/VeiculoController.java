package com.prologapp.desafio.presentation.v1.controllers;

import com.prologapp.desafio.application.dtos.PagedDataDTO;
import com.prologapp.desafio.application.dtos.VeiculoRequestDTO;
import com.prologapp.desafio.application.dtos.VeiculoResponseDTO;
import com.prologapp.desafio.application.services.IVeiculoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/veiculos")
@Validated
public class VeiculoController {

    private IVeiculoService veiculoService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public PagedDataDTO<VeiculoResponseDTO> obterPneusPaginados(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return veiculoService.obterPneusPaginados(page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{placa}")
    public VeiculoResponseDTO obterVeiculoEspecifico(@PathVariable String placa) {
        return veiculoService.obterVeiculoEspecifico(placa);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void regisrarVeiculo(@Valid @RequestBody VeiculoRequestDTO veiculo) {
        veiculoService.regisrarVeiculo(veiculo);
    }
}
