package thuvien.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thuvien.entity.Publisher;
import thuvien.repository.PublisherRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public List<Publisher> findAll() { return publisherRepository.findAll(); }

    public Publisher findById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà xuất bản ID: " + id));
    }

    public void save(Publisher publisher) { publisherRepository.save(publisher); }

    public void deleteById(Long id) { publisherRepository.deleteById(id); }
}