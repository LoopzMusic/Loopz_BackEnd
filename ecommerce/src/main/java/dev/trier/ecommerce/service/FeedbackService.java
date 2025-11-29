package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.feedback.FeedbackListResponseDto;
import dev.trier.ecommerce.dto.feedback.FeedbackRequestDto;
import dev.trier.ecommerce.dto.feedback.FeedbackResponseDto;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.model.acoesUsuario.FeedbackModel;
import dev.trier.ecommerce.repository.FeedbackRepository;
import dev.trier.ecommerce.repository.ProdutoRepository;
import dev.trier.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public FeedbackResponseDto criarFeedback(FeedbackRequestDto feedbackCriarDto) {

        if (feedbackCriarDto.nuAvaliacao() < 1 || feedbackCriarDto.nuAvaliacao() > 5) {
            throw new IllegalArgumentException("Avaliação deve estar entre 1 a 5 estrelas");
        }

        if (feedbackCriarDto.dsComentario() != null && feedbackCriarDto.dsComentario().length() > 500) {
            throw new IllegalArgumentException("Comentário não pode ter mais de 500 caracteres");
        }

        UsuarioModel usuario = usuarioRepository.findById(feedbackCriarDto.cdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        ProdutoModel produto = produtoRepository.findById(feedbackCriarDto.cdProduto())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        FeedbackModel feedback = new FeedbackModel();
        feedback.setUsuario(usuario);
        feedback.setProduto(produto);
        feedback.setNuAvaliacao(feedbackCriarDto.nuAvaliacao());
        feedback.setDsComentario(feedbackCriarDto.dsComentario());

        FeedbackModel salvo = feedbackRepository.save(feedback);

        return new FeedbackResponseDto(
                salvo.getCdFeedBack(),
                salvo.getNuAvaliacao(),
                salvo.getDsComentario(),
                salvo.getUsuario().getCdUsuario(),
                salvo.getUsuario().getNmCliente(),
                salvo.getProduto().getCdProduto(),
                salvo.getProduto().getNmProduto(),
                salvo.getDtCriacao()
        );
    }

    @Transactional(readOnly = true)
    public List<FeedbackListResponseDto> listarFeedback() {
        return feedbackRepository.findAll().stream()
                .map(model -> new FeedbackListResponseDto(
                        model.getCdFeedBack(),
                        model.getNuAvaliacao(),
                        model.getDsComentario(),
                        model.getUsuario().getNmCliente(),
                        model.getProduto().getNmProduto(),
                        model.getDtCriacao()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FeedbackResponseDto buscarPorId(Integer id) {
        FeedbackModel model = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback não encontrado"));

        return new FeedbackResponseDto(
                model.getCdFeedBack(),
                model.getNuAvaliacao(),
                model.getDsComentario(),
                model.getUsuario().getCdUsuario(),
                model.getUsuario().getNmCliente(),
                model.getProduto().getCdProduto(),
                model.getProduto().getNmProduto(),
                model.getDtCriacao()

        );
    }

    @Transactional(readOnly = true)
    public List<FeedbackListResponseDto> listarPorProduto(Integer cdProduto) {
        if (!produtoRepository.existsById(cdProduto)) {
            throw new IllegalArgumentException("Produto não encontrado");
        }

        return feedbackRepository.findByProduto_CdProduto(cdProduto).stream()
                .map(model -> new FeedbackListResponseDto(
                        model.getCdFeedBack(),
                        model.getNuAvaliacao(),
                        model.getDsComentario(),
                        model.getUsuario().getNmCliente(),
                        model.getProduto().getNmProduto(),
                        model.getDtCriacao()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FeedbackListResponseDto> listarPorUsuario(Integer cdUsuario) {
        if (!usuarioRepository.existsById(cdUsuario)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        return feedbackRepository.findByUsuario_CdUsuario(cdUsuario).stream()
                .map(model -> new FeedbackListResponseDto(
                        model.getCdFeedBack(),
                        model.getNuAvaliacao(),
                        model.getDsComentario(),
                        model.getUsuario().getNmCliente(),
                        model.getProduto().getNmProduto(),
                        model.getDtCriacao()
                ))
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