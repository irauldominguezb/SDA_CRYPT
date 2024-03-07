<template>
    <b-card class="mt-3 close" cols="12" lg="8" bg-variant="transparent">
        <template #header>
            <b-col class="d-flex justify-content-between align-items-center">
                <h6 class="text-secondary">
                    filtrar {{ searchType === 'range' ? ' por rango de fechas' : ' por fecha específica' }}
                </h6>
                <b-button-toolbar
                >
                    <b-button-group
                        size="sm"
                        class="mr-2"
                    >
                    <b-button title="Buscar" variant="primary" @click="setSearchMethod(searchType)">
                        <b-icon icon="search" aria-hidden="true"></b-icon>
                    </b-button>
                    <b-button title="Limpiar campos" variant="primary" @click="resetAndGetMovies()">
                        <b-icon icon="backspace" aria-hidden="true"></b-icon>
                    </b-button>
                    <b-button @click="hideComponent()" title="Ocultar filtrado" variant="primary">
                        <b-icon icon="caret-up" aria-hidden="true"></b-icon>
                    </b-button>

                    </b-button-group>
                </b-button-toolbar>
            </b-col>
        </template>
        <b-row v-if="searchType === 'range'">
            <b-col cols="12" lg="4" class="mb-1">
                <b-form-group
                    id="fieldset-start-date"
                    class="dateInput"
                >
                    <label label-for="input-start-date" class="mandatory-field">Fecha de inicio:</label>
                    <b-form-input
                        id="input-start-date"
                        type="date"
                        v-model="dates.start"
                    >
                    </b-form-input>
                </b-form-group>
            </b-col>
            <b-col cols="12" lg="4">
                <b-form-group
                    id="fieldset-end-date"
                    class="dateInput"
                >
                    <label label-for="input-end-date mandatory-field" class="mandatory-field">Fecha de fin:</label>
                    <b-form-input
                        id="input-end-date"
                        type="date"
                        v-model="dates.end"
                        @blur="validDates(dates)"
                    >
                    </b-form-input>
                </b-form-group>
            </b-col>
        </b-row>
        <b-row v-else> 
            <b-col cols="12" lg="4" class="dateInput">
                <b-form-group
                    id="fieldset-end-date"
                    invalid-feedback="La fecha de publicación es obligatoria."
                >
                    <label label-for="input-end-date mandatory-field" class="mandatory-field">Fecha de publicación: </label>
                    <b-form-input
                        id="input-end-date"
                        type="date"
                        :state="v$.date.$dirty ? !v$.date.$error : null"
                        v-model="v$.date.$model"
                    >
                    </b-form-input>
                </b-form-group>
            </b-col>
        </b-row>
    </b-card>
</template>

<script>
import moment from 'moment'
import moviesServices from "@/services/Movie"
import { useVuelidate } from '@vuelidate/core'
import { required } from '@vuelidate/validators'

export default {
    name: 'FilterDatesComponent',
    props: {
        searchType:{
            type: String,
            required: true
        },
    },
    setup(){
        return { v$: useVuelidate() }
    },
    data(){
        return {
            dates: {
                start: "",
                end: ""
            },
            date: "",
            areDatesValid: false
        }
    },
    methods: {
        validDates(dates){
            const {start, end} = dates
            const startDate = moment(start)
            const endDate = moment(end)
            if(startDate.isValid && endDate.isValid){
                if(!startDate.isAfter(endDate, 'day')){
                    if(!startDate.isSame(endDate, 'day')){
                        this.areDatesValid = true
                    }else{
                        this.areDatesValid = false
                        this.$swal.fire({
                            icon: 'warning',
                            title: 'Oops...',
                            text: 'Las fechas no pueden ser iguales!',
                            showCancelButton: false,
                            showConfirmButton: false,
                            timer: 2000
                        })
                        this.resetDates()
                    }
                }else{
                    this.areDatesValid = false
                    this.$swal.fire({
                        icon: 'warning',
                        title: 'Oops...',
                        text: 'La fecha de inicio debe ser menor a la fecha de fin!',
                        showCancelButton: false,
                        showConfirmButton: false,
                        timer: 2000
                    })
                    this.resetDates()
                }
            }else{
                this.areDatesValid = false
                this.$swal.fire({
                    icon: 'warning',
                    title: 'Oops...',
                    text: 'Las fechas no son válidas!',
                    showCancelButton: false,
                    showConfirmButton: false,
                    timer: 2000
                })
                this.resetDates()
            }
        },
        setSearchMethod(searchType){
            if(searchType === 'range'){
                if(this.areDatesValid){
                    this.getByDatesRange()
                }
            }else{
                if(this.v$.date.$valid){
                    this.getBySpecificDate()
                }
            }
        },
        hideComponent(){
            let closeElements = document.getElementsByClassName('close');
            for(let i = 0; i < closeElements.length; i++){
                setTimeout(() => {
                    this.$emit('hideComponent')
                }, 500);
                closeElements[i].classList.add('componentOut')
            }
        },
        async getByDatesRange(){
           console.log(this.dates)
        },
        async getBySpecificDate(){
            try {
                const releaseDate = moment(this.date).format('YYYY-MM-DD')
                console.log(releaseDate)
                this.$emit('showLoading')
                const {status, data:{content}} = await moviesServices.getMoviesByRealeaseDate(releaseDate)
                if(status === 200){
                    this.$emit('hideLoading')
                    this.$emit('addMoviesFromFilter', content)
                }
           } catch (error) {
            this.$emit('hideLoading')
           } 
        },
        resetDates(){
            this.dates = {
                start: "",
                end: ""
            }
        },
        resetAndGetMovies(){
            this.resetDates()
            this.$emit('getMovies')
            this.resetDates()
        }
    },
    validations: {
        dates: {
            start: {
                required
            },
            end: {
                required
            }
        },
        date: {
            required
        }
    }
}
</script>

<style scoped>
.mandatory-field::after{
    content: " * ";
    color: red
}



@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}


@keyframes fallIn {
  0% {
    opacity: 0;
    transform: translateY(-50px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}


@keyframes fallOut {
    0% {
        opacity: 1;
        transform: translateY(0);
    }
    100% {
        opacity: 0;
        transform: translateY(-50px);
    }
    
}

.componentOut{
    animation: fallOut 0.5s ease-in;
}


.dateInput{
    animation: fallIn 0.5s ease-in;

}

.animation{
    animation: fadeIn 0.5s ease-in;
}


</style>