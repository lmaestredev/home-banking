const app = Vue.createApp({
    data(){
        return {
            email:"",
            password:"",
            sigInError:false,
            eyePassword:false,
        }
    },
    created(){
        
        
    },
    computed:{
        
    },
    methods:{
        logIn(){
            let validacionEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

            if (this.email != "" && this.password != "") {
                if (validacionEmail.test(this.email)) {
                    axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => 

                        window.location.href = "accounts.html")

                    .catch(error => {
                        this.email = "";
                        this.password = "";
                        setTimeout(this.showSigInError, 1000);
                        setTimeout(this.showSigInError, 3500);
                    });

                }else{
                    Swal.fire({
                        position: 'top-end',
                        icon: 'error',
                        title:"Please, enter a valid email address. For example: user@email.com" ,
                        showConfirmButton: false,
                        timer:2000,
                    })
                }
            }else{
                Swal.fire({
                    position: 'top-end',
                    icon: 'error',
                    title:"Please fill out username/password" ,
                    showConfirmButton: false,
                    timer:2000,
                })
            }
        },
        showPassword(){
            this.eyePassword = !this.eyePassword
        },
        showSigInError(){
            this.sigInError = !this.sigInError
        }
    },
})

const consola = app.mount("#app")
