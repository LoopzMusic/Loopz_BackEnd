package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.empresa.criacao.EmpresaCriadaRespostaDto;
import dev.trier.ecommerce.dto.empresa.criacao.EmpresaCriarDto;
import dev.trier.ecommerce.dto.empresa.modificacao.UpdateEmpresaDto;
import dev.trier.ecommerce.dto.empresa.response.EmpresaListResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.EmpresaModel;
import dev.trier.ecommerce.repository.EmpresaRepository;
import dev.trier.ecommerce.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import dev.trier.ecommerce.exceptions.EntityInUseException;
import dev.trier.ecommerce.repository.ItemPedidoRepository;
import dev.trier.ecommerce.repository.ProdutoRespository;

@AllArgsConstructor
@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRespository produtoRespository;

    @Transactional
    public EmpresaCriadaRespostaDto criarEmpresa(EmpresaCriarDto empresadto) {
        EmpresaModel empresaModel = new EmpresaModel();
        empresaModel.setNmFantasia(empresadto.nmFantasia());
        empresaModel.setNmRazao(empresadto.nmRazao());
        empresaModel.setNuCNPJ(empresadto.nuCNPJ());
        empresaModel.setNuTelefone(empresadto.nuTelefone());
        empresaModel.setDsCidade(empresadto.dsCidade());
        empresaModel.setDsEstado(empresadto.dsEstado());
        empresaModel.setDsEndereco(empresadto.dsEndereco());
        empresaModel.setNuEndereco(empresadto.nuEndereco());
        EmpresaModel salvar = empresaRepository.save(empresaModel);

        return new EmpresaCriadaRespostaDto(
                salvar.getNmFantasia(),
                salvar.getNmRazao(),
                salvar.getNuCNPJ()
        );
    }

    @Transactional
    public EmpresaCriadaRespostaDto atualizarEmpresa(Integer cdEmpresa, UpdateEmpresaDto updateEmpresaDto) {
        EmpresaModel empresaModel = empresaRepository.findByCdEmpresa(cdEmpresa)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa não encontrada: " + cdEmpresa));

        Utils.copyNonNullProperties(updateEmpresaDto, empresaModel, "cdEmpresa", "produtos", "flAtivo");

        EmpresaModel salvo = empresaRepository.save(empresaModel);

        return new EmpresaCriadaRespostaDto(
                salvo.getNmFantasia(),
                salvo.getNmRazao(),
                salvo.getNuCNPJ()
        );
    }

    @Transactional
    public List<EmpresaListResponseDto> listarTodos(){
        return empresaRepository.findAll()
                .stream()
                .map(empresa -> new EmpresaListResponseDto(
                        empresa.getCdEmpresa(),
                        empresa.getNmFantasia(),
                        empresa.getNmRazao(),
                        empresa.getNuCNPJ(),
                        empresa.getNuTelefone(),
                        empresa.getDsCidade(),
                        empresa.getDsEstado(),
                        empresa.getDsEndereco(),
                        empresa.getNuEndereco(),
                        empresa.getFlAtivo(),

                        empresa.getProdutos() == null ? List.of() : empresa.getProdutos()
                                .stream()
                                .map(dev.trier.ecommerce.model.ProdutoModel::getCdProduto)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public EmpresaListResponseDto listarEmpresaCNPJ(String nuCNPJ){
        EmpresaModel empresa = empresaRepository.findByNuCNPJ(nuCNPJ)
                .orElseThrow(() -> new RecursoNaoEncontradoException("CNPJ "+ nuCNPJ + " não encontrado."));

        return new EmpresaListResponseDto(
                empresa.getCdEmpresa(),
                empresa.getNmFantasia(),
                empresa.getNmRazao(),
                empresa.getNuCNPJ(),
                empresa.getNuTelefone(),
                empresa.getDsCidade(),
                empresa.getDsEstado(),
                empresa.getDsEndereco(),
                empresa.getNuEndereco(),
                empresa.getFlAtivo(),

                empresa.getProdutos() == null ? List.of() : empresa.getProdutos()
                        .stream()
                        .map(dev.trier.ecommerce.model.ProdutoModel::getCdProduto)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void removerEmpresa(Integer cdEmpresa) {

        if (!empresaRepository.existsById(cdEmpresa)) {
            throw new RecursoNaoEncontradoException("Empresa não encontrada: " + cdEmpresa);
        }

        boolean usada = itemPedidoRepository.existsByProduto_Empresa_CdEmpresa(cdEmpresa);
        if (usada) {
            throw new EntityInUseException("Empresa possui produtos vinculados a pedidos e não pode ser removida.");
        }

        produtoRespository.deleteAllByEmpresa_CdEmpresa(cdEmpresa);

        empresaRepository.deleteById(cdEmpresa);
    }

 }
