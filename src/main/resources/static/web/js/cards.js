const app = Vue.createApp({
    data(){
        return {
            client:[],
            cards:[],
            creditCards:[],
            debitCards:[],
            btnDeleteCard:false,
            dateNow: new Date(),
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
                this.cards = response.data.current.cardsDTO.filter(card => card.delete == "no")
                this.creditCards = this.filterByCardType(this.cards, "CREDIT")
                this.debitCards = this.filterByCardType(this.cards, "DEBIT")

                let loader = this.$refs.loader
                let cardSection = this.$refs.cardSection

                loader.style.display = "none";
                cardSection.style.display = "block";
            })
        },
        filterByCardType(arrayCards, cardType){
            if (cardType === "CREDIT") {
                creditCards = arrayCards.filter((card) =>{
                    if(card.cardType === "CREDIT" && card.delete == "no"){
                        return card
                    }
                })
                return creditCards
            }else if( cardType === "DEBIT"){
                debitCards = arrayCards.filter((card) =>{
                    if( card.cardType === "DEBIT" && card.delete == "no"){
                        return card
                    }
                })

                return debitCards
            }    
        },
        logOut(){
            axios.post('/api/logout',{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => window.location.href = "index.html")
        },
        createCard(){
            window.location.href="create-cards.html"
        },
        changeDates(date){ 
            return moment(date).format("MM/YY")
        },
        createCardError(){
            Swal.fire({
                position: 'top-end',
                icon: 'error',
                title: 'You already have number of cards allowed',
                showConfirmButton: false,
                timer:3000,
            })
        },
        changeDeleteBtnValue(){
            this.btnDeleteCard = !this.btnDeleteCard
        },
        deleteACreditCard(cardNumber){

            Swal.fire({
                title: 'Are you sure to dele this card?',
                customClass: "alertConfirm",
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: "Yes, I'm sure!"

            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete(`/api/deleteCard/${cardNumber}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response =>{
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: `${response.data} `,
                            showConfirmButton: false,
                            timer: 3500
                        })
                        location.reload()
                    })
                    .catch(error =>{
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
        deleteADebitCard(cardNumber){
            Swal.fire({
                title: 'Are you sure to dele this card?',
                customClass: "alertConfirm",
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: "Yes, I'm sure!"

            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete(`/api/deleteCard/${cardNumber}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response =>{
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: `${response.data} `,
                            showConfirmButton: false,
                            timer: 3500
                        })
                        location.reload()
                    })
                    .catch(error =>{
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
        }
        
    },
})

const consola = app.mount("#app")