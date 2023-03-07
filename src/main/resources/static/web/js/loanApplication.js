const {createApp} = Vue 

createApp({
	data(){
        return {
            objectClient: {},
            accounts: {},
            loansOfClx: [],
            loans: {},
            loanId: undefined,
            amountSelected: undefined,
            paymentSelected: undefined,
            accountNumber: "",
            loanSelected: {},
            listOfPayments: {}
           
            
        }

	},
	created(){
        this.loadDataClx(),
        this.loadDataLoans()
        
	},

    methods: {
        loadDataClx(){
            axios.get("http://localhost:8080/api/clients/current")
                .then(response =>{
                   this.objectClient =response.data
                   this.accounts = this.objectClient.accounts
                   this.loansOfClx = this.objectClient.loans
                   this.cards = this.objectClient.cards  
                      
                })
        },
        changed(){
            this.loanSelected = this.loans.filter(loan => loan.id == this.loanId)
            this.listOfPayments = this.loanSelected[0].payments
            
        },
        loadDataLoans(){
            axios.get("/api/loans")
            .then(response => {
                this.loans = response.data
            })
            .catch(error => console.log(error.data))
        },
        applyLoan(){
            axios.post("/api/loans", {id: this.loanId, amount: this.amountSelected, payments: this.paymentSelected, accountNumber: this.accountNumber }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                Swal.fire({
                    icon: 'success',
                    title: `${response.data}`, 
                    text: `${response.status}: CREATED`,
                    showConfirmButton: false,
                    timer: 2000,
                })
                .then(response => { window.location.href = '/web/accounts.html'; })
            })
            .catch(error => {
                console.log(error)
                Swal.fire({
                    icon: 'error',
                    title: `${error.response.data}`, 
                    text: `${error.code}: ${error.response.status}`,
                    confirmButtonColor: '#FF4B2B',
      
                })
            })
        },
        alertApply(){
            Swal.fire({
                title: 'Are you sure you want to Apply to this Loan?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#FF4B2B',
                confirmButtonText: 'Confirm'
            }).then((response) => {
                if (response.isConfirmed) {
                    this.applyLoan();
            }
            })
        },
        monthlyPayments(){
            let amountWithFees = this.amountSelected * 0.2 + this.amountSelected
            let monthlyPayment = (amountWithFees / this.paymentSelected).toLocaleString('de-DE', { style: 'currency', currency: 'USD' })
            return monthlyPayment
        }
        
    }
	
}).mount('#app')