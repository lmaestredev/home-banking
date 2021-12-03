const app = Vue.createApp({
    data(){
        return {
            transactionData:[],
            userId:"",
            client:[],
            datesArray:[],
            account:[],
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');

            axios.get(`/api/accounts/${id}`)
            .then(response => {
                this.account = response.data.account
                this.transactionData = response.data.account.transactionDTO
                this.sortAccounts(response.data.account.transactionDTO)

                let loader = this.$refs.loader
                let accountsSection = this.$refs.accountsSection

                loader.style.display = "none";
                accountsSection.style.display = "block";
            })

            axios.get('/api/clients/current')
            .then(response => {
               this.client = response.data.current
            })

            
        },
        changeDates(date){ 
            return moment(date).format("DD/MM/YYYY - hh:mm a")
        },
        sortAccounts(arrayAccounts){
            arrayAccounts.sort((a, b) => {
               if (a.id < b.id) {return 1 }
               if (a.id > b.id) {return -1}
               return 0
            })
            return arrayAccounts
        },
        logOut(){
            axios.post('/api/logout').then(response => window.location.href = "index.html")
        }
    },
})

const consola = app.mount("#app")