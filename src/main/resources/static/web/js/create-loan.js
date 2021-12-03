const app = Vue.createApp({
    data(){
        return {
            client:[],
            dataLoan:{
                loanName:"",
                loanAmount:null,
                loanPayments:null,
                loanPercentage:null
            },
        }
    },
    created(){
        this.loadData();  
    },
    computed:{
    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then((response) => {
                this.client = response.data.current

                let loader = this.$refs.loader
                let accountsSection = this.$refs.accountsSection
    
                loader.style.display = "none";
                accountsSection.style.display = "block";
            })
            .catch(e => console.log(e))
        },
        logOut(){
            axios.post('/api/logout').then(response => window.location.href = "index.html")
        },
        createNewLoan(){
                        
            Swal.fire({
                title: 'Are you sure to create this loan?',
                customClass: "alertConfirm",
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: "Yes, I'm sure!"
            }).then((result) => {
                if (result.isConfirmed) {

                    if (!isNaN(this.dataLoan.loanAmount)) {
                        axios.post('/api/admin/newLoan',`loanName=${this.dataLoan.loanName}&maxAmount=${this.dataLoan.loanAmount}&payments=${this.dataLoan.loanPayments}&percentage=${this.dataLoan.loanPercentage}`)
                        .then((response) => {

                            Swal.fire({
                                position: 'top-end',
                                icon: 'success',
                                title:`${response.data}` ,
                                showConfirmButton: false,
                                timer:2500,
                            }) 

                            location.reload()
                        }
                        )
                        .catch((err) => 
                            Swal.fire({
                                position: 'top-end',
                                icon: 'error',
                                title:`${err.response.data }`,
                                showConfirmButton: false,
                                timer:2000,
                            })  
                        )
                    }else{
                        Swal.fire({
                            position: 'top-end',
                            icon: 'error',
                            title:"Please enter a valid amount" ,
                            showConfirmButton: false,
                            timer:2000,
                        })  
                    }
                }
            }) 
            
            
        }
    },   
})

const consola = app.mount("#app")
