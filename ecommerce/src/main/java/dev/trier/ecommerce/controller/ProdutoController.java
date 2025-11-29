package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.produto.criacao.CriarProdutoResponseDto;
import dev.trier.ecommerce.dto.produto.response.*;
import dev.trier.ecommerce.dto.produto.criacao.ProdutoCriarDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/produto")
@Tag(name = "Produto", description = "Produto de criação e modificação do produto")
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;



    @PostMapping(path = "/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Criar produto", description = "Cria um novo produto com imagem (multipart/form-data)")
    public ResponseEntity<CriarProdutoResponseDto> criarProduto(@ModelAttribute @Valid ProdutoCriarDto produtoCriarDto) {  //ModelAttribute para receber multipart
        CriarProdutoResponseDto produtoCriado = produtoService.criarProduto(produtoCriarDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produtoCriado);
    }



    @GetMapping(path = "/listar/todos")
    @Operation(summary = "Listar produtos", description = "Lista todos os produtos")
    public ResponseEntity<List<ListarProdutosResponseDto>> listarTodos() {
        //var lista = produtoService.listarProdutos();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoService.listarProdutos());
    }

    @GetMapping("/{cdProduto}/detalhes")
    @Operation(summary = "Listar produto por mais detalhes", description = "Retorna um produto por ID com todos os detalhes, incluindo a imagem.")
    public ResponseEntity<ListarProdutoDetalhadoResponseDto> listarProdutoMaisDetalhes(@PathVariable Integer cdProduto){
        return ResponseEntity.ok(produtoService.buscarProdutoPorIdMaisDetalhes(cdProduto));
    }


    @PatchMapping(path = "/alterar/{cdProduto}/texto", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualizar texto do produto", description = "Atualiza apenas os campos de texto do produto (nome, valor, categoria, acessório, descrição e empresa)")
    public ResponseEntity<ProdutoTextUpdateResponseDto> atualizarProdutoTexto(
            @PathVariable Integer cdProduto,
            @RequestBody @Valid ProdutoTextUpdateDto updateProdutoDto) {

        ProdutoTextUpdateResponseDto response = produtoService.atualizarProdutoTexto(updateProdutoDto, cdProduto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/alterar/{cdProduto}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Atualizar imagem do produto", description = "Atualiza apenas a imagem de um produto")
    public ResponseEntity<Void> atualizarImagemProduto(
            @PathVariable Integer cdProduto,
            @RequestParam("imgProduto") MultipartFile imgProduto) {

        produtoService.atualizarImagemProduto(cdProduto, imgProduto);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(path = "/{cdProduto}/imagem")
    @Transactional
    @Operation(summary = "Obter imagem do produto", description = "Retorna a imagem do produto em formato JPEG")
    public ResponseEntity<byte[]> listarImagem(@PathVariable Integer cdProduto) {
        byte[] imagem = produtoService.buscarImagemProduto(cdProduto);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagem);
    }


    @GetMapping(path = "/{cdProduto}/idProduto")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os dados do produto pelo código")
    public ResponseEntity<Optional<ProdutoIdResponseDto>> buscarProdutoId(@PathVariable Integer cdProduto) {
        return ResponseEntity.ok()
                .body(produtoService.buscarProdutoId(cdProduto));
    }


    @GetMapping(path = "/{nmProduto}")
    @Operation(summary = "Buscar produto por nome", description = "Retorna os dados do produto pelo nome")
    public ResponseEntity<Optional<ProdutoNomeResponseDto>> listarProdutoNome(@PathVariable String nmProduto) {
        Optional<ProdutoNomeResponseDto> response = produtoService.listarProdutoNome(nmProduto);
        return ResponseEntity.ok()
                .body(response);
    }

    @DeleteMapping("/delete/{cdProduto}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Integer cdProduto) {
        try {
            produtoService.removerProduto(cdProduto);
            return ResponseEntity.noContent().build();
        } catch (dev.trier.ecommerce.exceptions.EntityInUseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
