//package com.reservas.demo_reservas.initializer;
//
//import com.reservas.demo_reservas.entities.Rol;
//import com.reservas.demo_reservas.entities.Usuario;
//import com.reservas.demo_reservas.enums.RoleName;
//import com.reservas.demo_reservas.repositories.RolRepository;
//import com.reservas.demo_reservas.repositories.UsuarioRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@RequiredArgsConstructor
//public class DataInitializer {
//
//    private final RolRepository rolRepository;
//    private final UsuarioRepository usuarioRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Bean
//    CommandLineRunner initDatabase() {
//        return args -> {
//            //Crear rol ADMIN
//            Rol adminRol = rolRepository
//                    .findByNombre(RoleName.ADMIN)
//                    .orElseGet(() -> {
//                       Rol rol = new Rol();
//                       rol.setNombre(RoleName.ADMIN);
//
//                       return rolRepository.save(rol);
//                    });
//            //Crear rol GESTOR
//            rolRepository
//                    .findByNombre(RoleName.GESTOR)
//                    .orElseGet(() -> {
//                        Rol rol = new Rol();
//                        rol.setNombre(RoleName.GESTOR);
//
//                        return rolRepository.save(rol);
//                    });
//
//            //Crear usuario ADMIN
//            boolean existAdmin = usuarioRepository.existsByEmail("admin@mail.com");
//
//            if (!existAdmin) {
//
//                Usuario admin = new Usuario();
//                admin.setNombre("Administrador");
//                admin.setEmail("admin@mail.com");
//
//                admin.setPassword(
//                        passwordEncoder.encode("admin")
//                );
//                admin.setActivo(true);
//                admin.setRol(adminRol);
//
//                //usuarioRepository.save(admin);
//
//                //System.out.println("Usuario ADMIN creado correctamente");
//            }
//        };
//    }
//}
