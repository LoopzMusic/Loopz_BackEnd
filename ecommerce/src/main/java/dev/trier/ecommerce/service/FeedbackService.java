package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.feedback.FeedbackListResponseDto;
import dev.trier.ecommerce.dto.feedback.FeedbackRequestDto;
import dev.trier.ecommerce.dto.feedback.FeedbackResponseDto;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.model.acoesUsuario.FeedbackModel;
import dev.trier.ecommerce.repository.FeedbackRepository;
import dev.trier.ecommerce.repository.ProdutoRespository;
import dev.trier.ecommerce.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRespository produtoRespository;

    @Transactional
    public FeedbackResponseDto criarFeedback(FeedbackRequestDto dto) {

        if (dto.nuAvaliacao() < 1 || dto.nuAvaliacao() > 5) {
            throw new IllegalArgumentException("Avaliação deve estar entre 1 a 5 estrelas");
        }

        if (dto.dsComentario() != null && dto.dsComentario().length() > 500) {
            throw new IllegalArgumentException("Comentário não pode ter mais de 500 caracteres");
        }

        UsuarioModel usuario = usuarioRepository.findById(dto.cdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        ProdutoModel produto = produtoRespository.findById(dto.cdProduto())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        FeedbackModel feedback = new FeedbackModel();
        feedback.setUsuario(usuario);
        feedback.setProduto(produto);
        feedback.setNuAvaliacao(dto.nuAvaliacao());
        feedback.setDsComentario(dto.dsComentario());

        FeedbackModel salvo = feedbackRepository.save(feedback);

        FeedbackResponseDto response = new FeedbackResponseDto();
        response.setCdFeedback(salvo.getCdFeedBack());
        response.setNuAvaliacao(salvo.getNuAvaliacao());
        response.setDsComentario(salvo.getDsComentario());
        response.setCdUsuario(salvo.getUsuario().getCdUsuario());
        response.setNmUsuario(salvo.getUsuario().getNmCliente());
        response.setCdProduto(salvo.getProduto().getCdProduto());
        response.setNmProduto(salvo.getProduto().getNmProduto());


        return response;
    }

    public List<FeedbackListResponseDto> listarTodos() {
        return feedbackRepository.findAll().stream()
                .map(model -> {
                    FeedbackListResponseDto dto = new FeedbackListResponseDto();
                    dto.setCdFeedback(model.getCdFeedBack());
                    dto.setNuAvaliacao(model.getNuAvaliacao());
                    dto.setDsComentario(model.getDsComentario());
                    dto.setNmUsuario(model.getUsuario().getNmCliente());
                    dto.setNmProduto(model.getProduto().getNmProduto());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public FeedbackResponseDto buscarPorId(Integer id) {
        FeedbackModel model = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback não encontrado"));

        FeedbackResponseDto response = new FeedbackResponseDto();
        response.setCdFeedback(model.getCdFeedBack());
        response.setNuAvaliacao(model.getNuAvaliacao());
        response.setDsComentario(model.getDsComentario());
        response.setCdUsuario(model.getUsuario().getCdUsuario());
        response.setNmUsuario(model.getUsuario().getNmCliente());
        response.setCdProduto(model.getProduto().getCdProduto());
        response.setNmProduto(model.getProduto().getNmProduto());


        return response;
    }

    public List<FeedbackListResponseDto> listarPorProduto(Integer cdProduto) {
        if (!produtoRespository.existsById(cdProduto)) {
            throw new IllegalArgumentException("Produto não encontrado");
        }

        return feedbackRepository.findByProduto_CdProduto(cdProduto).stream()
                .map(model -> {
                    FeedbackListResponseDto dto = new FeedbackListResponseDto();
                    dto.setCdFeedback(model.getCdFeedBack());
                    dto.setNuAvaliacao(model.getNuAvaliacao());
                    dto.setDsComentario(model.getDsComentario());
                    dto.setNmUsuario(model.getUsuario().getNmCliente());
                    dto.setNmProduto(model.getProduto().getNmProduto());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<FeedbackListResponseDto> listarPorUsuario(Integer cdUsuario) {
        if (!usuarioRepository.existsById(cdUsuario)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        return feedbackRepository.findByUsuario_CdUsuario(cdUsuario).stream()
                .map(model -> {
                    FeedbackListResponseDto dto = new FeedbackListResponseDto();
                    dto.setCdFeedback(model.getCdFeedBack());
                    dto.setNuAvaliacao(model.getNuAvaliacao());
                    dto.setDsComentario(model.getDsComentario());
                    dto.setNmUsuario(model.getUsuario().getNmCliente());
                    dto.setNmProduto(model.getProduto().getNmProduto());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void removerFeedback(Integer id) {
        if (!feedbackRepository.existsById(id)) {
            throw new IllegalArgumentException("Feedback não encontrado");
        }
        feedbackRepository.deleteById(id);
    }
}