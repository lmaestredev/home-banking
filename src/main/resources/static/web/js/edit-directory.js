const app = Vue.createApp({
    data(){
        return {
            client:[],
            directory:[],
            idToDelete:null,
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then((response) => {
                this.client = response.data.current
                this.directory = response.data.current.clientFrequentContactsDTO
                this.sortById(this.directory, "ascendent")

                let loader = this.$refs.loader
                let transactionSection = this.$refs.transactionSection

                loader.style.display = "none";
                transactionSection.style.display = "block";
            })
            .catch(e => console.log(e))
        },
        sortById(arrayAccounts, orden){// ascendent or descendent

            if(orden === "ascendent"){
                arrayAccounts.sort((a, b) => {
                    if (a.id < b.id) {return -1 }
                    if (a.id > b.id) {return 1}
                    return 0 
                })
                return arrayAccounts
            }
        },
        logOut(){
            axios.post('/api/logout').then(response => window.location.href = "index.html")
        },
        deleteContact(id){
            
             axios.delete(`/api/deleteContact/${id}`)
             .then(response =>{
                location.reload()
             })
             .catch(err =>{
                console.log(err.response.data)
             })
        },
        
    },
})

const consola = app.mount("#app")
