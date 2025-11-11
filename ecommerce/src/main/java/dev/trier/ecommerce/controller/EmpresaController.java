package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.empresa.criacao.EmpresaCriadaRespostaDto;
import dev.trier.ecommerce.dto.empresa.criacao.EmpresaCriarDto;
import dev.trier.ecommerce.dto.empresa.modificacao.UpdateEmpresaDto;
import dev.trier.ecommerce.dto.empresa.response.EmpresaListResponseDto;
import dev.trier.ecommerce.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/empresa")
@Tag(name = "Empresa", description = "Capacidade de criação e modificação da empresa")
@CrossOrigin("*")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;


    @PostMapping("/criar")
    @Operation(summary = "Criar empresa", description = "Cria uma nova empresa")
    public ResponseEntity<EmpresaCriadaRespostaDto> criarEmpresa(@RequestBody @Valid EmpresaCriarDto empresaCriarDto){
        EmpresaCriadaRespostaDto empresaCriado = empresaService.criarEmpresa(empresaCriarDto);
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaCriado);
    }

    @CrossOrigin
    @GetMapping("/listar/todos")
    @Transactional
    @Operation(summary = "Listar empresas", description = "Lista todas as empresas cadastradas")
    public ResponseEntity<List<EmpresaListResponseDto>> listar(){
        var lista = empresaService.listarTodos();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lista);
    }


    @GetMapping("/listarCNPJ/{nuCNPJ}")
    @Operation(summary = "Buscar empresa por CNPJ", description = "Retorna os dados da empresa a partir do CNPJ informado")
    public ResponseEntity<EmpresaListResponseDto> listarEmpresaCNPJ(@PathVariable String nuCNPJ){
        EmpresaListResponseDto empresa = empresaService.listarEmpresaCNPJ(nuCNPJ);
        return ResponseEntity.ok(empresa);
    }

    @PutMapping("/update/{cdEmpresa}")
    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa pelo código")
    public ResponseEntity<EmpresaCriadaRespostaDto> atualizarEmpresa(@PathVariable Integer cdEmpresa,
                                                                     @RequestBody @Valid UpdateEmpresaDto updateEmpresaDto) {
        try {
            EmpresaCriadaRespostaDto atualizado = empresaService.atualizarEmpresa(cdEmpresa, updateEmpresaDto);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{cdEmpresa}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Integer cdEmpresa) {
        try {
            empresaService.removerEmpresa(cdEmpresa);
            return ResponseEntity.noContent().build();
        } catch (dev.trier.ecommerce.exceptions.EntityInUseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
