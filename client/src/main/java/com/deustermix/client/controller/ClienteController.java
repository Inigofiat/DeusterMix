package com.deustermix.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Libro;
import com.deustermix.client.data.Receta;
import com.deustermix.client.data.Usuario;
import com.deustermix.client.service.UsuarioServiceProxy;

import java.util.List;

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
        usuarioServiceProxy.logout(token);     
        token = null;
        return "redirect:/";
    }

    @GetMapping("/principal")
    public String mostrarPrincipal(Model model, HttpServletRequest request) {
        addAttributes(model, request);
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
    
    // Añadir mapeo para la página de perfil
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, HttpServletRequest request) {
        addAttributes(model, request);
        return "perfil";
    }

    @PostMapping("/crear-receta")
    public String crearReceta(@RequestBody Receta receta, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.crearReceta(token, receta);
            redirectAttributes.addFlashAttribute("exito", "Receta creada con éxito");
            return "redirect:/principal";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la receta: " + e.getMessage());
            return "redirect:/principal";
        }
    }

    @PostMapping("/eliminar-receta")
    public String eliminarReceta(@RequestParam("idReceta") Long idReceta, @RequestParam(value = "redirectUrl", required = false) String redirectUrl, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.eliminarReceta(token, idReceta);
            redirectAttributes.addFlashAttribute("exito", "Receta eliminada con éxito");
            return "redirect:" + (redirectUrl != null ? redirectUrl : "/");  
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la receta: " + e.getMessage());
            return "redirect:/principal";
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

    @PostMapping("/crear-libro")
    public String crearLibro(@RequestBody Libro libro, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.crearLibro(token, libro);
            redirectAttributes.addFlashAttribute("exito", "Libro creado con éxito");
            return "redirect:/principal";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el libro: " + e.getMessage());
            return "redirect:/principal";
        }
    }

    @PostMapping("/eliminar-libro")
    public String eliminarLibro(@RequestParam("idLibro") Long idLibro, @RequestParam(value = "redirectUrl", required = false) String redirectUrl, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.eliminarLibro(token, idLibro);
            redirectAttributes.addFlashAttribute("exito", "Libro eliminado con éxito");
            return "redirect:" + (redirectUrl != null ? redirectUrl : "/");  
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el libro: " + e.getMessage());
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

    
}