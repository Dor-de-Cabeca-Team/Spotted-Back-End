package com.Rede_Social.Service;

import com.Rede_Social.DTO.Consulta.ComplaintDTO;
import com.Rede_Social.DTO.Mapper.ComplaintDTOMapper;
import com.Rede_Social.Entity.CommentEntity;
import com.Rede_Social.Entity.ComplaintEntity;
import com.Rede_Social.Entity.PostEntity;
import com.Rede_Social.Entity.UserEntity;
import com.Rede_Social.Exception.Post.PostNotFoundException;
import com.Rede_Social.Exception.User.UserNotFoundException;
import com.Rede_Social.Repository.CommentRepository;
import com.Rede_Social.Repository.ComplaintRepository;
import com.Rede_Social.Repository.PostRepository;
import com.Rede_Social.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public String save(ComplaintDTO complaint) {
        try {
            UserEntity user = userRepository.findById(complaint.idUser()).orElseThrow(UserNotFoundException::new);
            PostEntity post = postRepository.findById(complaint.idPost()).orElseThrow(PostNotFoundException::new);

            CommentEntity comment = null;
            if (complaint.idComment() != null) {
                comment = commentRepository.findById(complaint.idComment()).orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
            }

            ComplaintEntity complaintEntity = new ComplaintEntity();
            complaintEntity.setUser(user);
            complaintEntity.setPost(post);
            complaintEntity.setComment(comment);

            complaintRepository.save(complaintEntity);

            return "Denuncia criada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para salvar a reclamação no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para salvar a reclamação no repository: " + e.getMessage());
        }
    }

    public String update(ComplaintDTO complaint, UUID uuid) {
        try {
            ComplaintEntity existingComplaint = complaintRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Reclamação não existe no banco"));

            UserEntity user = userRepository.findById(complaint.idUser()).orElseThrow(UserNotFoundException::new);
            PostEntity post = postRepository.findById(complaint.idPost()).orElseThrow(PostNotFoundException::new);

            CommentEntity comment = null;
            if (complaint.idComment() != null) {
                comment = commentRepository.findById(complaint.idComment()).orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
            }

            existingComplaint.setUser(user);
            existingComplaint.setPost(post);
            existingComplaint.setComment(comment);

            complaintRepository.save(existingComplaint);

            return "Denuncia atualizada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para atualizar a reclamação no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para atualizar a reclamação no repository: " + e.getMessage());
        }
    }

    public String delete(UUID uuid) {
        try {
            complaintRepository.deleteById(uuid);
            return "Reclamação deletada";
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para deletar a reclamação no repository: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para deletar a reclamação no repository: " + e.getMessage());
        }
    }

    public ComplaintDTO findById(UUID uuid) {
        ComplaintEntity complaint = complaintRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Reclamação não encontrada no banco"));
        return ComplaintDTOMapper.toComplaintDto(complaint);
    }

    public List<ComplaintDTO> findAll() {
        try {
            List<ComplaintEntity> complaintEntityList = complaintRepository.findAll();
            List<ComplaintDTO> complaintDTOList = new ArrayList<>();

            for(ComplaintEntity complaint : complaintEntityList){
                complaintDTOList.add(ComplaintDTOMapper.toComplaintDto(complaint));
            }

            return complaintDTOList;
        } catch (Exception e) {
            System.out.println("Erro no service, não deu para listar as reclamações do banco: " + e.getMessage());
            throw new RuntimeException("Erro no service, não deu para listar as reclamacoes: " + e.getMessage());
        }
    }
}
