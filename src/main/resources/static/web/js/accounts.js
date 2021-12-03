const app = Vue.createApp({
    data(){
        return {
            client:[],
            accounts:[],
            loans:[],
            loansRepository:[],
            accountsRepository:[]
        }
    },
    created(){
        this.loadData();
        this.crearUrl();
        this.getLoansRepository();
        this.getAccountsRepository();
        // axios.get('http://localhost:8080/api/clients/current',{headers:{'accept':'application/xml'}})
        // .then(response =>
        //     console.log(response.data)
        // )
    },computed:{
    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then((response) => {
                this.client = response.data.current
                this.accounts = response.data.current.accountsDTO.filter(account => !account.deleted)
                this.loans = response.data.current.clientLoansDTO
                this.sortById(this.accounts, "ascendent")
                this.sortById(this.loans, "descendent")

                let loader = this.$refs.loader
                let accountsSection = this.$refs.accountsSection
                let adminItem1 = this.$refs.adminItem1
                let adminItem2 = this.$refs.adminItem2
                let adminItem3 = this.$refs.adminItem3
    
                loader.style.display = "none";
                accountsSection.style.display = "block";
                
                if(this.client.role == 'ADMIN' ){
                    adminItem1.style.display = "flex";
                    adminItem2.style.display = "flex";
                    adminItem3.style.display = "flex";
                }
                
            })
            .catch(e => console.log(e))           
        },
        getLoansRepository(){
            axios.get('/api/loan')
            .then(response => {
                this.loansRepository = response.data.loans
            })
            .catch(err =>console.log(err))
        },
        crearUrl(id){ 
            return `account.html?id=${id}`
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

            if (orden === "descendent") {
                arrayAccounts.sort((a, b) => {
                if (a.loanId < b.loanId) {return 1 }
                if (a.id > b.id) {return -1}
                return 0
                })
                return arrayAccounts
            }  
        },
        logOut(){
            axios.post('/api/logout').then(response => window.location.href = "index.html")
        },
        createAccount(){

            Swal.fire({
                customClass: "alertConfirm",
                title: 'What kind of account Do you want?',
                showDenyButton: true,
                showCancelButton: false,
                confirmButtonText: 'Savings',
                denyButtonText: `Checking`,
            }).then((result) => {
                 
                if (result.isConfirmed){
                    axios.post('/api/clients/current/accounts',`accountType=SAVINGS`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(function (response) {
                    
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: `${response.data} `,
                            showConfirmButton: false,
                            timer: 3000
                        })
                        location.reload()
                    })
                    .catch(function (error){
                        Swal.fire({
                            position: 'top-end',
                            icon: 'error',
                            title: `${error.response.data}`,
                            showConfirmButton: false,
                            timer:2000,
                        }) 
                    })
                }else if(result.isDenied) {

                    axios.post('/api/clients/current/accounts',`accountType=CHECKING`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(function (response) {
                        
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: `${response.data} `,
                            showConfirmButton: false,
                            timer: 3000
                        })
                        location.reload()
                    })
                    .catch(function (error){
                        Swal.fire({
                            position: 'top-end',
                            icon: 'error',
                            title: `${error.response.data}`,
                            showConfirmButton: false,
                            timer:2000,
                        }) 
                    })
                }
            })
            
        },
        redirecToApplyLoan(){
            window.location.href = "loan-application.html"
        },
        applyLoanError(){
            Swal.fire({
                position: 'top-end',
                icon: 'error',
                title: 'You already have loans allowed',
                showConfirmButton: false,
                timer:3000,
            })
        },
        deleteAccount(accountNumber){
            
            Swal.fire({
                title: 'Are you sure to dele this account?',
                customClass: "alertConfirm",
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: "Yes, I'm sure!"

            }).then((result) => {
                if (result.isConfirmed) {

                    axios.delete(`/api/deleteAccount/${accountNumber}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: `${response.data} `,
                            showConfirmButton: false,
                            timer: 3000
                        })
                        location.reload()
                    })
                    .catch(err =>{
                        Swal.fire({
                            position: 'top-end',
                            icon: 'error',
                            title: `${err.response.data}`,
                            showConfirmButton: false,
                            timer:3000,
                        })
                    })
                }
            })
        },
        deleteLoan(id){
            axios.delete(`/api/delete/loan/${id}`)
            .then(response =>{
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: `${response.data} `,
                    showConfirmButton: false,
                    timer: 3000
                })
                location.reload()
            })
            .catch(err => {console.log(err)})

        },
        getAccountsRepository(){
            axios.get('/api/accounts')
            .then(response => {
                this.accountsRepository = response.data.accounts
            })
        }
    },
})

const consola = app.mount("#app")