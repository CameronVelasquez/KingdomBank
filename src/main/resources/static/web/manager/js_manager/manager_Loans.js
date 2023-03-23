
const {createApp} = Vue 

createApp({
	data(){
        return {
            name: "",
            maxAmount: undefined,
            payments: [],
            fee: undefined,
            feePayments: [],
            objectLoans: {},
            paymentsListToSplit: [],
            paymentFeesListToSplit: [],
            
            
        }

	},
	created(){
        this.loadData()
	},

    methods: {
        loadData() {
            axios.get("/api/loans")
            .then(response => {
            this.objectLoans = response.data
            
            })
            .catch(err => console.log(err))
        },
        change(){ 
            this.paymentFeesListToSplit = [this.feePayments.split(",")][0]
            console.log(this.paymentFeesListToSplit)

        },
        addLoan(){
            this.paymentsListToSplit = this.payments.split(",")
            this.paymentFeesListToSplit = this.feePayments.split(",")
            console.log(this.paymentFeesListToSplit)
            axios.post("/api/admin/loans", {name:this.name, maxAmount:this.maxAmount, payments:this.paymentsListToSplit, fee:this.fee, feePayments:this.paymentFeesListToSplit}, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response =>{
                location.href = "http://localhost:8080/web/manager/manager_Loans.html"
            })
        },
        feeAsPercentage(number){
          return "%"+ Math.round(number * 100 - 100 )
        },
        logOut(){
            axios.post("/api/logout")
            .then( response => {
                Swal.fire({
                    icon: 'success',
                    title: `Successfully Log out!`, 
                    text: `${response.status}: OK`,
                    showConfirmButton: false,
                    timer: 2000,
                })
                .then(response => {
                    location.href = "/web/index.html"
                })
            })
            .catch(error => console.log(error))
        } 
   
    }
	
}).mount('#app')