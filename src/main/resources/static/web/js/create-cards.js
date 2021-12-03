const app = Vue.createApp({
    data(){
        return {
            client:[],
            cardType:"",
            cardColor:"",
            cards:[],
            creditCards:[],
            debitCards:[]
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
                this.cards = response.data.current.cardsDTO
                this.creditCards = this.filterByCardType(this.cards, "CREDIT")
                this.debitCards = this.filterByCardType(this.cards, "DEBIT")
            })
        },
        logOut(){
            axios.post('/api/logout').then(response => window.location.href = "index.html")
        },
        goToCards(){
            window.location.href = "cards.html"
        },
        createNewCard(){

            axios.post('/api/clients/current/cards',`cardType=${this.cardType}&cardColor=${this.cardColor}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(function (response) {
                
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: "Your card has been created successfully",
                    showConfirmButton: false,
                    timer:3000,
                })
                window.location.href = "cards.html"
                
            })
            .catch(function (error){
                Swal.fire({
                    position: 'top-end',
                    icon: 'error',
                    title: `${error.response.data}`,
                    showConfirmButton: false,
                    timer:3000,
                })
                
            } )
            
            
        },
        filterByCardType(arrayCards, cardType){
            if (cardType === "CREDIT") {
                creditCards = arrayCards.filter((card) =>{
                    return card.cardType === "CREDIT"
                })
                return creditCards
            }else if( cardType === "DEBIT"){
                debitCards = arrayCards.filter((card) =>{
                    return card.cardType === "DEBIT"
                })

                return debitCards
            }    
        }
        
    },
})

const consola = app.mount("#app")

