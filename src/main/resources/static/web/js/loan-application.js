const app = Vue.createApp({
    data(){
        return {
            client:[],
            accounts:[],
            loans:[],
            loanSelectedPercentage:"",
            payments:[],
            pricePerPayments:"",
            totalPriceToPay:"",
            dataLoan:{
                loanName:"",
                loanAmount:"",
                loanPayments:"",
                destinationAccountNumber:"",
            },
        }
    },
    created(){
        this.loadData();  
        this.getLoans();
    },
    computed:{
        paymentsByLoanName(){
            for (let i = 0; i < this.loans.length; i++) {
                if (this.loans[i].name == this.dataLoan.loanName) {
                    this.payments = this.loans[i].payments
                }
            }
            return this.payments
        },
        percentageFromloanSelectedPercentage(){
            for (let i = 0; i < this.loans.length; i++) {
                if (this.loans[i].name == this.dataLoan.loanName) {
                    this.loanSelectedPercentage = this.loans[i]
                }
            }
            return this.loanSelectedPercentage.percentage
        }
    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then((response) => {
                this.client = response.data.current
                this.accounts = this.client.accountsDTO
            })
            .catch(e => console.log(e))
        },
        logOut(){
            axios.post('/api/logout').then(response => window.location.href = "index.html")
        },
        getLoans(){
            axios.get('/api/loan')
            .then((response) => {
                this.loans=response.data.loans
            })
            .catch(e => console.log(e))
        },
        postLoan(){
                        
            Swal.fire({
                title: 'Are you sure to apply for this loan?',
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
                        axios.post('/api/loan',{loanName:this.dataLoan.loanName,amount:this.dataLoan.loanAmount,payments:this.dataLoan.loanPayments,destinationAccountNumber:this.dataLoan.destinationAccountNumber})
                        .then((response) => {

                            Swal.fire({
                                customClass: "alertConfirm",
                                title:'Send!',
                                text:'Your loan has been sent.',
                                icon:'success',
                                timer:2000,
                            })

                            window.location.href = "accounts.html" 
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
