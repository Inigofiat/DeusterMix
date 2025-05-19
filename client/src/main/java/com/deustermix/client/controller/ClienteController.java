package com.deustermix.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deustermix.client.data.Cliente;
import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Libro;
import com.deustermix.client.data.Receta;
import com.deustermix.client.data.Usuario;
import com.deustermix.client.service.UsuarioServiceProxy;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ClienteController {
    @Autowired
    protected UsuarioServiceProxy usuarioServiceProxy;
	
    protected String token;
    
    public void addAttributes(Model model, HttpServletRequest request) {
		String currentUrl = ServletUriComponentsBuilder.fromRequestUri(request).toUriString();
		model.addAttribute("currentUrl", currentUrl); // Makes current URL available in all templates
	}

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                Model model) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "registro";
    }

    @PostMapping("/registro")
    public String regitrar(@RequestBody Usuario usuario, 
                                  RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.registrar(usuario);
            redirectAttributes.addFlashAttribute("exito", "Usuario registrado con exito");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/login")
    public String showLogin(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                             Model model) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "login";
    }

    @PostMapping("/login")
    public String login (@RequestParam String email, 
                         @RequestParam String contrasena, 
                         RedirectAttributes redirectAttributes, 
                         Model model) {
        try {
            Credenciales credenciales = new Credenciales(email, contrasena);
            token = usuarioServiceProxy.login(credenciales);
            usuarioServiceProxy.getDetalleUsuario(token).email();
            return "redirect:/principal";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Credenciales incorrectas");
            return "redirect:/";
        }
    }
    
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.logout(token);     
            token = null;
            redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada correctamente");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cerrar sesión");
            return "redirect:/";
        }
    }

    @GetMapping("/principal")
    public String mostrarPrincipal(Model model, HttpServletRequest request) {
        try {
            List<Receta> recetasDestacadas = usuarioServiceProxy.getRecetas().stream()
                .limit(3)
                .collect(Collectors.toList());
            model.addAttribute("recetasDestacadas", recetasDestacadas);
            addAttributes(model, request);
        } catch (Exception e) {
            System.err.println("Error al cargar recetas destacadas: " + e.getMessage());
        }
        return "principal";
    }
    
    @GetMapping("/recetas")
    public String consultarRecetas(Model model, HttpServletRequest request) {
        addAttributes(model, request);
        return "consultarRecetas";
    }
    
    @GetMapping("/api/recetas")
    @ResponseBody
    public ResponseEntity<List<Receta>> getRecetas() {
        try {
            List<Receta> recetas = usuarioServiceProxy.getRecetas();
            return ResponseEntity.ok(recetas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/api/recetas/{id}")
    @ResponseBody
    public ResponseEntity<Receta> getRecetaById(@PathVariable Long id) {
        try {
            Receta receta = usuarioServiceProxy.obtenerReceta(id);
            return ResponseEntity.ok(receta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/receta/{id}")
    public String verReceta(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            addAttributes(model, request);
            return "detalleReceta";
        } catch (Exception e) {
            return "redirect:/recetas";
        }
    }

    @GetMapping("/detalleRecetaLibro/{id}")
    public String verRecetaLibro(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            addAttributes(model, request);
            return "detalleRecetaLibro";
        } catch (Exception e) {
            return "redirect:/recetas";
        }
    }
    
    // Añadir mapeo para la página de libros
    @GetMapping("/libros")
    public String consultarLibros(Model model, HttpServletRequest request) {
        addAttributes(model, request);
        return "consultarLibros";
    }

    @GetMapping("/api/libros")
    @ResponseBody
    public ResponseEntity<List<Libro>> getLibros() {
        try {
            List<Libro> libros = usuarioServiceProxy.getLibros();
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/libros/{id}")
    @ResponseBody
    public ResponseEntity<Libro> getLibroById(@PathVariable Long id) {
        try {
            Libro libro = usuarioServiceProxy.obtenerLibro(id);
            return ResponseEntity.ok(libro);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/libro/{id}")
    public String verLibro(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            addAttributes(model, request);
            return "detalleLibro";
        } catch (Exception e) {
            return "redirect:/libros";
        }
    }
    
    @GetMapping("/api/cliente")
@ResponseBody
public ResponseEntity<Cliente> getDetalleCliente() {
    try {
        // Verificar si el usuario está autenticado
        if (token == null) {
            return ResponseEntity.status(401).build(); // No autorizado
        }
        
        // Obtener información del cliente usando el token
        Cliente cliente = usuarioServiceProxy.getDetalleCliente(token);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(cliente);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(null);
    }
}

// Actualizar el método mostrarPerfil para usar getDetalleCliente en lugar de getDetalleUsuario
@GetMapping("/perfil")
public String mostrarPerfil(Model model, HttpServletRequest request) {
    try {
        // Verificar si el usuario está autenticado
        if (token == null) {
            return "redirect:/login?redirectUrl=/perfil";
        }
        
        // Obtener información del cliente actual usando el token
        Cliente cliente = usuarioServiceProxy.getDetalleCliente(token);
        if (cliente == null) {
            return "redirect:/login";
        }
        
        // Añadir el cliente al modelo para acceder en la vista
        model.addAttribute("cliente", cliente);
        addAttributes(model, request);
        return "miPerfil";
    } catch (Exception e) {
        return "redirect:/";
    }
}

    @PostMapping("/guardar-receta")
    public String guardarReceta(@RequestParam("idReceta") Long idReceta, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.guardarReceta(token, idReceta); // Call service to save the recipe
            redirectAttributes.addFlashAttribute("exito", "Receta guardada con éxito");
            return "redirect:/principal";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la receta: " + e.getMessage());
            return "redirect:/principal";
        }
    }
    
    @PostMapping("/eliminar-receta-favorita")
    public String eliminarRecetaFavorita(@RequestParam("idReceta") Long idReceta, @RequestParam(value = "redirectUrl", required = false) String redirectUrl, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.eliminarRecetaFavorita(token, idReceta);
            redirectAttributes.addFlashAttribute("exito", "Receta eliminada de favoritos con éxito");
            return "redirect:" + (redirectUrl != null ? redirectUrl : "/principal");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la receta de favoritos: " + e.getMessage());
            return "redirect:/principal";
        }
    }

    @DeleteMapping("/api/recetas/favorito/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarRecetaFavorita(@PathVariable("id") Long idReceta) {
        System.out.println("[Controller] Eliminando receta favorita: " + idReceta);
        try {
            if (token == null) {
                System.out.println("[Controller] No hay token disponible");
                return ResponseEntity.status(401).build();
            }
            
            usuarioServiceProxy.eliminarRecetaFavorita(token, idReceta);
            System.out.println("[Controller] Receta eliminada exitosamente");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("[Controller] Error eliminando receta: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/cliente/recetas/guardadas")
@ResponseBody
public ResponseEntity<List<Receta>> getRecetasGuardadas(@RequestParam(value = "tokenUsuario", required = false) String tokenUsuario) {
    System.out.println("[Cliente] Token de sesión actual: " + this.token);
    System.out.println("[Cliente] Token recibido como parámetro: " + tokenUsuario);
    
    // Usar el token de la sesión si no se proporciona uno en el parámetro
    String tokenToUse = tokenUsuario != null && !tokenUsuario.equals("null") ? tokenUsuario : this.token;
    
    if (tokenToUse == null || tokenToUse.isEmpty()) {
        System.out.println("[Cliente] No hay token disponible");
        return ResponseEntity.ok(List.of()); // Devolver lista vacía en lugar de 401
    }
    
    try {
        List<Receta> recetasGuardadas = usuarioServiceProxy.getRecetasGuardadas(tokenToUse);
        System.out.println("[Cliente] Recetas obtenidas exitosamente: " + 
            (recetasGuardadas != null ? recetasGuardadas.size() : 0));
        
        return ResponseEntity.ok(recetasGuardadas != null ? recetasGuardadas : List.of());
    } catch (Exception e) {
        System.err.println("[Cliente] Error obteniendo recetas: " + e.getMessage());
        return ResponseEntity.ok(List.of()); // Devolver lista vacía en lugar de error
    }
}

@GetMapping("/mis-recetas")
public String mostrarMisRecetas(Model model, HttpServletRequest request) {
    try {
        // Verificar si el usuario está autenticado
        if (token == null) {
            return "redirect:/login?redirectUrl=/mis-recetas";
        }
        
        addAttributes(model, request);
        return "misRecetas";
    } catch (Exception e) {
        return "redirect:/principal";
    }
}

    @PostMapping("/guardar-libro")
    public String guardarLibro(@RequestParam("idLibro") Long idLibro, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.guardarLibro(token, idLibro);
            redirectAttributes.addFlashAttribute("exito", "Libro guardado con éxito");
            return "redirect:/libros";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el libro: " + e.getMessage());
            return "redirect:/libros";
        }
    }

    @GetMapping("/procesar-pago")
    public String procesarPago(@RequestParam("id") Long idLibro, Model model, HttpServletRequest request) {
        try {
            // Verificar si el usuario está autenticado
            if (token == null) {
                return "redirect:/login?redirectUrl=/procesar-pago?id=" + idLibro;
            }
            
            // Obtener información del libro
            Libro libro = usuarioServiceProxy.obtenerLibro(idLibro);
            if (libro == null) {
                return "redirect:/libros";
            }
            
            addAttributes(model, request);
            model.addAttribute("idLibro", idLibro);
            return "procesarPago";
        } catch (Exception e) {
            return "redirect:/libros";
        }
    }

    @PostMapping("/finalizar-compra")
    public String finalizarCompra(@RequestParam("idLibro") Long idLibro, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si el usuario está autenticado
            if (token == null) {
                redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para completar la compra");
                return "redirect:/login?redirectUrl=/procesar-pago?id=" + idLibro;
            }
            
            System.out.println("Finalizando compra para libro ID: " + idLibro + " con token: " + token);
            
            // Llamar al servicio para guardar el libro en la biblioteca del usuario
            usuarioServiceProxy.guardarLibro(token, idLibro);
            
            redirectAttributes.addFlashAttribute("exito", "¡Compra realizada con éxito! El libro ha sido añadido a tu biblioteca.");
            return "redirect:/libros";
        } catch (Exception e) {
            System.err.println("Error al procesar la compra: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al procesar la compra: " + e.getMessage());
            return "redirect:/libros";
        }
    }

    @GetMapping("/mis-libros")
public String mostrarMisLibros(Model model, HttpServletRequest request) {
    try {
        if (token == null) {
            return "redirect:/login?redirectUrl=/mis-libros";
        }
        
        addAttributes(model, request);
        return "misLibros";
    } catch (Exception e) {
        return "redirect:/principal";
    }
}

@GetMapping("/api/cliente/libros/comprados")
@ResponseBody
public ResponseEntity<List<Libro>> getLibrosComprados() {
    System.out.println("[Cliente] Obteniendo libros comprados. Token: " + this.token);
    
    if (token == null) {
        System.out.println("[Cliente] No hay token disponible");
        return ResponseEntity.ok(List.of());
    }
    
    try {
        List<Libro> librosComprados = usuarioServiceProxy.getLibrosComprados(token);
        System.out.println("[Cliente] Libros obtenidos exitosamente: " + 
            (librosComprados != null ? librosComprados.size() : 0));
        
        return ResponseEntity.ok(librosComprados != null ? librosComprados : List.of());
    } catch (Exception e) {
        System.err.println("[Cliente] Error obteniendo libros: " + e.getMessage());
        return ResponseEntity.ok(List.of());
    }
}

@GetMapping("/api/libros/{id}/recetas")
@ResponseBody
public ResponseEntity<List<Receta>> getRecetasDeLibro(@PathVariable Long id) {
    try {
        System.out.println("[ClienteController] Solicitando recetas del libro: " + id);
        Libro libro = usuarioServiceProxy.obtenerLibro(id);
        if (libro == null || libro.recetas() == null) {
            System.out.println("[ClienteController] Libro no encontrado o sin recetas");
            return ResponseEntity.ok(List.of());
        }
        System.out.println("[ClienteController] Recetas encontradas: " + libro.recetas().size());
        return ResponseEntity.ok(libro.recetas());
    } catch (Exception e) {
        System.err.println("[ClienteController] Error: " + e.getMessage());
        return ResponseEntity.badRequest().build();
    }
}

@GetMapping("/api/usuario/tiene-libro/{id}")
@ResponseBody
public ResponseEntity<Boolean> verificarLibroComprado(@PathVariable Long id) {
    System.out.println("[ClienteController] Verificando libro comprado: " + id);
    
    if (token == null) {
        System.out.println("[ClienteController] No hay token disponible");
        return ResponseEntity.ok(false);
    }
    
    try {
        Boolean tieneLibro = usuarioServiceProxy.verificarLibroComprado(token, id);
        System.out.println("[ClienteController] Resultado verificación: " + tieneLibro);
        return ResponseEntity.ok(tieneLibro);
    } catch (Exception e) {
        System.err.println("[ClienteController] Error: " + e.getMessage());
        return ResponseEntity.ok(false);
    }
}
}