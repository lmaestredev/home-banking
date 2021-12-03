const app = Vue.createApp({
    data(){
        return {
            clients:[],
            clientsJSON:[],
            clientAdded:{
                firstName:"",
                lastName:"",
                email:""
            },
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get('/rest/clients')
            .then((response) => {
                this.clients = response.data._embedded.clients;
                this.clientsJSON = response.data._embedded;
            })
        },
        addClient(){
            let clientAdded ={
                firstName:"",
                firstName:"",
                email:"",
            }

            let validacionEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;


            if (this.clientAdded.firstName != "" && this.clientAdded.lastName != "" && this.clientAdded.email != "") {
                if(validacionEmail.test(this.clientAdded.email) ){
                    clientAdded.firstName = this.clientAdded.firstName
                    clientAdded.lastName = this.clientAdded.lastName
                    clientAdded.email = this.clientAdded.email

                    this.postClient(clientAdded)
                }else{
                    window.alert("Ingrese un email valido please")
                }
            }else{
                window.alert("Faltan datos para enviar el formulario")
            }
                    

        },
        postClient(client){
            axios.post('/rest/clients', this.clientAdded)
            .then(response =>{
                this.loadData()
                this.clientAdded.firstName = "";
                this.clientAdded.lastName= "";
                this.clientAdded.email = "";
            })
            .catch(error => console.log(error.message))
        }
    },
})

const consola = app.mount("#app")