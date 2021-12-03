const app = Vue.createApp({
    data(){
        return {
            firstName:"",
            lastName:"",
            email:"",
            password:"",
            password2:"",
            inputTerms:false,
            eyePassword:false,
        }
    },
    created(){  
    },
    methods:{
        signUp(){

            let validacionEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

            if (this.firstName != "" && this.lastName != "" && this.email != "" && this.password != "" && this.inputTerms && this.password2 != "") {
                

                    if (validacionEmail.test(this.email)) {

                        if (this.password == this.password2) {
                            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                            .then(response => {
                                
                                axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                                .then(response => {
                                    Swal.fire({
                                        position: 'top-end',
                                        icon: 'success',
                                        title: `${response.data} `,
                                        showConfirmButton: false,
                                        timer: 1000
                                    })
                                })
                                .then(response => {
                                    window.location.href = "accounts.html"
                                })
    
                            }).catch(error =>  {
                                Swal.fire({
                                    position: 'top-end',
                                    icon: 'error',
                                    title:`${error.response.data}` ,
                                    showConfirmButton: false,
                                    timer:2000,
                                })  
                            });   
                        }else{
                            Swal.fire({
                                position: 'top-end',
                                icon: 'error',
                                title:`Passwords are not equals` ,
                                showConfirmButton: false,
                                timer:2000,
                            }) 
                        }
                        
    
                    }else{
                        Swal.fire({
                            position: 'top-end',
                            icon: 'error',
                            title:"Please, enter a valid email address. For example: user@email.com" ,
                            showConfirmButton: false,
                            timer:3000,
                        })  
                    }
                
                
            }else{
                Swal.fire({
                    position: 'top-end',
                    icon: 'error',
                    title:"Please, complet all the fields" ,
                    showConfirmButton: false,
                    timer:2000,
                })
            }

        },
        showPassword(){
            this.eyePassword = !this.eyePassword
        }
    },
})

const consola = app.mount("#app")
