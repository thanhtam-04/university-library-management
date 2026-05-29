package thuvien.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thuvien.entity.Role;
import thuvien.entity.Role.RoleName;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // ✅ SỬA: String → RoleName
    Optional<Role> findByName(RoleName name);
}