document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("search-recipes");
    const recipeCards = document.querySelectorAll(".recipe-card");

    searchInput.addEventListener("input", (event) => {
        const query = event.target.value.toLowerCase();

        recipeCards.forEach((card) => {
            const title = card.querySelector("h2").textContent.toLowerCase();
            if (title.includes(query)) {
                card.style.display = "block";
            } else {
                card.style.display = "none";
            }
        });
    });
});

document.addEventListener("keyup", e=>{

    if(e.target.matches(".buscador")){

        document.querySelectorAll(".marcaInfo").forEach(marca =>{

            if(marca.textContent.toLowerCase().includes(e.target.value.toLowerCase())){
                marca.classList.remove("filtro")
            } else{
                marca.classList.add("filtro")
            }
            
        })

        document.querySelectorAll(".dispositivoInfo").forEach(dispositivo =>{

            if(dispositivo.textContent.toLowerCase().includes(e.target.value.toLowerCase())){
                dispositivo.classList.remove("filtro")
            } else{
                dispositivo.classList.add("filtro")
            }
            
        })


        document.querySelectorAll(".modeloInfo").forEach(modelo =>{

            if(modelo.textContent.toLowerCase().includes(e.target.value.toLowerCase())){
                modelo.classList.remove("filtro")
            } else{
                modelo.classList.add("filtro")
            }
            
        })
    }


})