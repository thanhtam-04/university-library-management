package thuvien.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thuvien.entity.ContactMessage;
import thuvien.repository.ContactMessageRepository;
import thuvien.dto.request.ContactRequest;

@Service
public class ContactService {
    @Autowired
    private ContactMessageRepository repository;

    public void saveContact(ContactRequest request) {
        ContactMessage msg = ContactMessage.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .subject(request.getSubject())
                .message(request.getMessage())
                .studentCode(request.getStudentCode())
                .build();
        repository.save(msg);
    }
}