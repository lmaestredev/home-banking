const app = Vue.createApp({
    data(){
        return {
            client:[],
            clientAccounts:[],
            transactionData:{
                originAccount:"",
                destinationAccount:"",
                amount:"",
                description:"",
                confirmToAdd:'false',
                contactType:'no',
            },
            sendingTo:"",
            setDestinationAccounts:[],
            directory: [],
            showConfirmToAdd:[],
            showContactType:false,
        }
    },
    created(){
        this.loadData(); 
    },
    computed:{
        filterDestinationAccounts(){
            this.setDestinationAccounts = this.clientAccounts.filter( element => element.number != this.transactionData.originAccount)
            return this.setDestinationAccounts
        },
        existsDestAccountDirectory(){         
            this.showConfirmToAdd = this.directory.filter(account => account.accountNumber.includes(this.transactionData.destinationAccount))          
            return this.showConfirmToAdd
        },
        changeConfirmToAddBoolean(){
            if(this.transactionData.confirmToAdd == 'true'){
                this.showContactType = true
                return this.showContactType
            }else{
                this.transactionData.contactType = 'no'
                return this.showContactType = false
            }
        }
    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then((response) => {
                this.client = response.data.current
                this.clientAccounts = response.data.current.accountsDTO
                this.directory = response.data.current.clientFrequentContactsDTO

                let loader = this.$refs.loader
                let transactionSection = this.$refs.transactionSection

                loader.style.display = "none";
                transactionSection.style.display = "block";
            })
            .catch(e => console.log(e))
        },
        logOut(){
            axios.post('/api/logout').then(response => window.location.href = "index.html")
        },
        transaction(){

            Swal.fire({
                title: 'Are you sure to make this transaction?',
                customClass: "alertConfirm",
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: "Yes, I'm sure!"

            }).then((result) => {
                if (result.isConfirmed) {

                    if (!isNaN(this.transactionData.amount)) {
                        if (this.sendingTo == 'others') {

                            let setToVerifyDestAccount = this.clientAccounts.filter(account => account.number == this.transactionData.destinationAccount)

                            if (setToVerifyDestAccount.length == 0 ) {
                                axios.post('/api/transactions',`originAccountNumber=${this.transactionData.originAccount}&destinationAccountNumber=${this.transactionData.destinationAccount}&amount=${this.transactionData.amount}&description=${this.transactionData.description}&confirmToAdd=${this.transactionData.confirmToAdd}&contactType=${this.transactionData.contactType}`)
                                .then(response => {

                                    Swal.fire({
                                        title: 'Do you want load the voucher?',
                                        customClass: "alertConfirm",
                                        icon: 'warning',
                                        showCancelButton: true,
                                        confirmButtonColor: '#3085d6',
                                        cancelButtonColor: '#d33',
                                        confirmButtonText: "Yes, I'm sure!"
                        
                                    }).then((result) => {
                                        if (result.isConfirmed) {


                                            axios.post("/api/transactions/pdf",`account1=VIN001&account2=VIN003&amount=100&description=test`,{responseType: 'blob'})
                                            .then(response =>{

                                                let file = response.headers['content-disposition']
                                                let fileName = decodeURI(file.substring(20))
                                                let link = document.createElement('a')
                                                link.href = URL.createObjectURL(response.data)
                                                link.download = fileName
                                                link.click()
                                                link.remove()

                                                Swal.fire({
                                                    position: 'top-end',
                                                    icon: 'success',
                                                    title:`Loading successfully` ,
                                                    showConfirmButton: false,
                                                    timer:2500,
                                                })

                                                location.reload()
                                            })
                                            .catch(err => {
                                                Swal.fire({
                                                    position: 'top-end',
                                                    icon: 'error',
                                                    title:`${err.response.data }`,
                                                    showConfirmButton: false,
                                                    timer:4500,
                                                }) 

                                                location.reload()
                                            })
                                        }
                                        else{
                                            Swal.fire({
                                                position: 'top-end',
                                                icon: 'success',
                                                title:`${response.data}` ,
                                                showConfirmButton: false,
                                                timer:2500,
                                            }) 
                
                                            location.reload()
                                        }
                                    })
                                    
                                })
                                .catch(error => {
                                    Swal.fire({
                                        position: 'top-end',
                                        icon: 'error',
                                        title:`${error.response.data }`,
                                        showConfirmButton: false,
                                        timer:4500,
                                    }) 
                                })  
                            }else{
                                Swal.fire({
                                    position: 'top-end',
                                    icon: 'error',
                                    title:'Please enter an account not yours',
                                    showConfirmButton: false,
                                    timer:3000,
                                }) 
                            }
                        }else if(this.sendingTo == 'myAccounts' || this.sendingTo == 'directory' ){
                            axios.post('/api/transactions', `originAccountNumber=${this.transactionData.originAccount}&destinationAccountNumber=${this.transactionData.destinationAccount}&amount=${this.transactionData.amount}&description=${this.transactionData.description}&confirmToAdd=false&contactType=''`)
                            .then(response => {
                                // Swal.fire({
                                //     position: 'top-end',
                                //     icon: 'success',
                                //     title:`${response.data}` ,
                                //     showConfirmButton: false,
                                //     timer:2500,
                                // }) 
                    
                                // location.reload()

                                Swal.fire({
                                    title: 'Do you want load the voucher?',
                                    customClass: "alertConfirm",
                                    icon: 'warning',
                                    showCancelButton: true,
                                    confirmButtonColor: '#3085d6',
                                    cancelButtonColor: '#d33',
                                    confirmButtonText: "Yes, I'm sure!"
                    
                                }).then((result) => {
                                    if (result.isConfirmed) {


                                        axios.post("/transactions/pdf",`account1=VIN001&account2=VIN003&amount=100&description=test`,{responseType: 'blob'})
                                        .then(response =>{

                                            let file = response.headers['content-disposition']
                                            let fileName = decodeURI(file.substring(20))
                                            let link = document.createElement('a')
                                            link.href = URL.createObjectURL(response.data)
                                            link.download = fileName
                                            link.click()
                                            link.remove()
                                            
                                            Swal.fire({
                                                position: 'top-end',
                                                icon: 'success',
                                                title:`Loading successfully` ,
                                                showConfirmButton: false,
                                                timer:2500,
                                            })

                                            location.reload()
                                        })
                                        .catch(err => {
                                            Swal.fire({
                                                position: 'top-end',
                                                icon: 'error',
                                                title:`${err.response.data }`,
                                                showConfirmButton: false,
                                                timer:4500,
                                            }) 

                                            
                                        })
                                    }
                                    else{
                                        Swal.fire({
                                            position: 'top-end',
                                            icon: 'success',
                                            title:`${response.data}` ,
                                            showConfirmButton: false,
                                            timer:2500,
                                        }) 
            
                                        location.reload()
                                    }
                                })
                            })
                            .catch(error => {
                                Swal.fire({
                                    position: 'top-end',
                                    icon: 'error',
                                    title:`${error.response.data}`,
                                    showConfirmButton: false,
                                    timer:3000,
                                }) 
                            }) 
                        }
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
            
        },
        redirecToeditDirectory(){
            window.location.href = "edit-directory.html"
        }
    }
   
})

const consola = app.mount("#app")
