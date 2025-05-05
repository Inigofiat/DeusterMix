package com.deustermix.client.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Receta;
import com.deustermix.client.data.Usuario;
import com.deustermix.client.service.UsuarioServiceProxy;

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
    public String mostrarPrincipal(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                Model model) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "principal";
    }

    @PostMapping("/crear-receta")
    public String createPost(@RequestBody Receta receta, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.crearReceta(token, receta);
            redirectAttributes.addFlashAttribute("exito", "Publicación creada con éxito");
            return "redirect:/principal";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la receta: " + e.getMessage());
            return "redirect:/principal";
        }
    }

    @PostMapping("/eliminar-receta")
    public String deletePost(@RequestParam("idReceta") Long idReceta, @RequestParam(value = "redirectUrl", required = false) String redirectUrl, RedirectAttributes redirectAttributes) {
        try {
            usuarioServiceProxy.eliminarReceta(token, idReceta);
            redirectAttributes.addFlashAttribute("exito", "Receta eliminada con éxito");
            return "redirect:" + (redirectUrl != null ? redirectUrl : "/");  
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la receta: " + e.getMessage());
            return "redirect:/principal";
        }
    }

}
