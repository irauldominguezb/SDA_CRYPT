<template>
    <div>
       <DeleteComponent v-show="onDrag" @showOverlay="showOverlay" @getMovies="getMovies" :onDrag="onDrag"/>
       <div class="container pt-3 pb-3">
        <b-card class="shadow-lg">
            <template slot="header" >
                <b-row class="pt-1">
                    <b-col md="6"><h5 class="text-secondary">Peliculas</h5></b-col>
                </b-row>
            </template>

            <b-card-body
                border-variant="white"
                header-bg-variant="white"
                header-text-variant="ssm"
                style="position: relative;" 
            >
                <b-row>
                    <b-col cols="12" class="mb-3" style="position: relative;">
                        <b-row>
                            <b-col cols="6">
                                <b-button class="m-1" variant="primary" @click="showSaveModal()">
                                    <b-icon icon="plus"></b-icon> Agregar
                                </b-button>
                                <b-dropdown variant="primary">
                                    <template #button-content>
                                        <b-icon icon="funnel" aria-hidden="true"></b-icon> Filtrar por
                                    </template>
                                    <b-dropdown-item-button @click="showComponent('specific')">
                                        Fecha específica de publicación
                                    </b-dropdown-item-button>
                                    <b-dropdown-divider></b-dropdown-divider>
                                    <b-dropdown-item-button @click="showComponent('range')">
                                        Rango de fechas
                                    </b-dropdown-item-button>
                                </b-dropdown>
                            </b-col>
                            <b-col cols="6">
                                <div>
                                    <b-input-group>
                                        <b-form-input
                                            id="search"
                                            pill
                                            :placeholder="'Buscar por ' + `${placehoderFilter}` + '...'"
                                            style="border: thin 0.4px gray;"
                                            v-model="search"
                                            @input="filter(search)"
                                        >
                                        </b-form-input>
                                        <template #append>
                                            <b-dropdown variant="primary" text style="">
                                                <b-dropdown-item @click="() => getFilter('name')">Nombre</b-dropdown-item>
                                                <b-dropdown-item @click= "() => getFilter('director')">Director</b-dropdown-item>
                                                <b-dropdown-item @click= "() => getFilter('gender')">Género</b-dropdown-item>
                                            </b-dropdown>
                                        </template>
                                    </b-input-group>    
                                </div>
                            </b-col>
                        </b-row>
                        <b-row>
                            <b-col>
                                <FilterDates 
                                    v-if="showSearch" 
                                    :searchType="searchType" 
                                    @hideComponent="hideComponent"
                                    @hideLoading="hideLoading"
                                    @showLoading="showLoading"
                                    @getMovies="getMovies"
                                    @addMoviesFromFilter="addMoviesFromFilter"
                                />
                            </b-col>
                        </b-row>
                        <b-row style="position: relative;">
                            <loading-componet v-if="isloading"/>
                            <template v-if="!isloading">
                                <b-col cols="12" class="mt-2">
                                    <hr>
                                </b-col>
                                <b-col cols="12" md="6" lg="4" class="mt-2 scroll-content" v-for="(movie, index) in movies" :key="index">
                                    <div v-if="movies.length != 0">
                                        <b-card pill draggable @dragstart="drag($event, movie, index)" bg-variant="white" text-variant="black" class="hover card-style mb-2" @click="Detail(movie.id)">
                                            <template #header>
                                                <b-row>
                                                    <b-col cols="12" class="d-flex text-truncate mt-1 justify-content-between align-items-center">
                                                        <b class="text-secondary">{{ movie.name }}</b>
                                                        <b-button variant="white" @click="changeStatus(movie.id)">
                                                            <b-icon class="delete-button" icon="x" style="color: #757577;" scale="1.5"></b-icon>
                                                        </b-button>
                                                    </b-col>
                                                </b-row>
                                            </template>
                                            <b-card-text>
                                                <b-row>
                                                    <b-col cols="12" class="text-black d-flex mb-1  justify-content-between align-items-center">
                                                        <b class="text-secondary">Director:</b> <label>{{ movie.director }}</label>
                                                    </b-col>
                                                    <b-col class="text-black d-flex mb-1 justify-content-between align-items-center"  cols="12">
                                                        <b class="text-secondary"> Duration:</b> {{ movie.duration }}
                                                    </b-col>
                                                    <b-col class="text-black d-flex mb-1 justify-content-between align-items-center" cols="12">
                                                        <b class="text-secondary">Genre:</b> {{ movie.gender.name }}
                                                    </b-col>
                                                    <b-col class="text-black mb-1 d-flex justify-content-between align-items-center" cols="12">
                                                        <b class="text-secondary">Availability: </b> 
                                                        <b-badge :class="movie.available ? 'success' : 'danger'">{{ movie.available ? 'Disponible' : 'No disponible' }}</b-badge>
                                                    </b-col>
                                                </b-row>
                                            </b-card-text>
                                        </b-card>
                                    </div>
                                </b-col>
                                <div v-show="movies.length === 0">
                                    <b-row>
                                        <b-col cols="12" class="d-flex justify-content-center">
                                            <b-row>
                                                <b-img
                                                    :src="require('@/assets/withoutRecords.jpg')"
                                                    style="height: 400px; width: 560px;"
                                                >
                                                </b-img>
                                            </b-row>
                                        </b-col>
                                    </b-row>
                                    <b-row>
                                        <h4 class="text-center">Sin películas que mostrar...</h4>
                                    </b-row>
                                </div>
                            </template>
                        </b-row>
                        <b-row v-if="!isloading">
                            <b-col cols="12" class="mt-1">
                                <b-row>
                                    <b-col cols ="6" md="4" class="align-self-center">
                                        <h6 class="text-secondary justify-content-left">
                                            Total de registros: {{ rows }}
                                        </h6>
                                    </b-col>
                                    <b-col cols ="6" md="4" class="d-flex justify-content-end order-md-5 align-self-center mb-4">
                                        <div class="d-inline justify-content-center align-items-right">
                                            <b-form-group
                                                class="text-right"
                                                label-cols="6"
                                                label-for="pages-select"
                                                label-size="md"
                                                style="font-weight: bold"
                                            >
                                                
                                                <b-input-group size="lg">
                                                    <b-form-select
                                                        id="pages-select"
                                                        style="border: solid 1px gray;"
                                                        v-model="perPage"
                                                        @change="getMovies()"
                                                    >
                                                    <template #first>
                                                        <option value="3">3</option>
                                                        <option value="5">5</option>
                                                        <option value="10">10</option>
                                                        <option value="15">15</option>
                                                        <option value="20">20</option>
                                                    </template>
                                                    </b-form-select>
                                                </b-input-group>
                                            </b-form-group>
                                        </div>
                                    </b-col>
                                    <b-col cols="12"  md="4"  class="d-flex justify-content-center order-md-3 mt-4 align-self-center">
                                        <b-pagination
                                            v-model="currentPage"
                                            pills
                                            @input="getMovies()"
                                            :total-rows="rows"
                                            :per-page="perPage"
                                            size="md"
                                        ></b-pagination>
                                    </b-col>
                                </b-row>
                            </b-col>
                        </b-row>
                    </b-col>
                </b-row>
            </b-card-body>
        </b-card>
        <SaveMovieVue
            @getMovies="getMovies()"
        />
       </div>
    </div>
</template>

<script>
import SaveMovieVue from './SaveMovie.vue'
import movieServices from '../../services/Movie'
import Loading from '@/components/Loading.vue'
import DeleteComponent from '../DeleteComponent.vue'
import FilterDates from './FilterDates.vue'
import moment from 'moment'
import {encrypt, decrypt} from "../../config/utils"
export default {
    components: {
        SaveMovieVue,
        'loading-componet': Loading,
        DeleteComponent,
        FilterDates,
    },
   
    name: 'MoviesPage',
    data(){
        return {
            movies :[],
            currentPage: 1,
            perPage: 5,
            rows: 0,
            isloading: true,
            placehoderFilter: 'nombre',
            filterBy: 'name',
            search: '',
            onDrag: false,
            showSearch: false,
            searchType: '',
            lastScrollPosition: 0,
            showElement: true
        }
    },
    methods: {
        async Detail(id) {
            let code = await encrypt(id.toString())
            this.$router.push({
                path:`/movieDetail/${encodeURIComponent(code)}`
            });
        },
        showSaveModal(){
            this.$bvModal.show("modal-save-movie");
        },
        drag(event, movie, index){
            document.getElementsByClassName('card-style')[index].style['z-index'] = 99
            event.dataTransfer.dropEffect = "move";
            event.dataTransfer.effectAllowed = "move";
            event.dataTransfer.setData("movieId", movie.id);
            event.dataTransfer.setData("indexCard", index);
            this.showOverlay()
        },
        showComponent(searchType){
            this.searchType = searchType
            this.showSearch = true
        },
        hideComponent(){
            this.showSearch = !this.showSearch
        },
        async getFilter(filter) {
            switch (filter) {
                case 'name':
                    this.filterBy = filter
                    this.placehoderFilter = 'nombre'
                    break
                case 'director':
                    this.filterBy = filter
                    this.placehoderFilter = 'director'
                    break
                case 'gender':
                    this.filterBy = filter
                    this.placehoderFilter = 'género'
                    break
            }
        },
        filter(search){
            const filterName = this.filterBy
            switch(filterName){
                case 'name':
                    this.getMoviesByName(search)
                    break
                case 'director':
                    this.getMoviesByDirector(search)
                    break
                case 'gender':
                    this.getMoviesByGender(search)
                    break
            }
        },
        async getMoviesByName(search){
            if(search){
                try {
                this.isloading = true
                const {status, data:{data} } = await movieServices.getMoviesByName(await encrypt(search))
                if(status === 200){
                    this.isloading = false
                    const {content} = JSON.parse(await decrypt(data))
                    this.movies = content
                }
                } catch (error) {
                    this.isloading = false
                    console.log("error",error)
                    } 
            }else{
                this.getMovies()
            }
        },
        async getMoviesByDirector(search){
            if(search || search != ""){
                try {
                this.isloading = true
                const {status, data:{data} } = await movieServices.getMoviesByDirector(await encrypt(search))
                if(status === 200){
                    this.isloading = false
                    const {content} = JSON.parse(await decrypt(data))
                    this.movies = content
                }
                } catch (error) {
                    this.isloading = false
                    console.log("error",error)
                } 
            }else{
                this.getMovies()
            }
        },
        async getMoviesByGender(search){
            if(search.length > 2){
                try {
                    this.isloading = true
                    const {status, data:{data} } = await movieServices.getMoviesByGender(await encrypt(search))
                if(status === 200){
                    this.isloading = false
                    const {content} = JSON.parse(await decrypt(data))
                    this.movies = content
                }
                } catch (error) {
                    this.isloading = false
                    console.log("error",error)
                } 
            }else{
                this.getMovies()
            }
        },   
        async getMovies(){
            this.isloading = true
            try {
                const pagination = {
                    size: this.perPage,
                    page: (this.currentPage-1)
                }
                const {status, data:{data}} = await movieServices.getMovies(pagination)
                if(status === 200){
                    this.isloading = false
                    const {content, totalElements} = JSON.parse(await decrypt(data))
                    this.movies = content
                    this.rows = totalElements 
                }
            } catch (error) {
                console.log("error",error)
            }
            this.isloading = false
        },
        addMoviesFromFilter(movies){
            this.movies = movies
        },
        changeStatus(id){
            this.$swal.fire({
                icon: "warning",
                title: "¿Estás seguro que deseas deshabilitar esta película?",
                showCancelButton: true,
                confirmButtonText: "Aceptar",
                cancelButtonText: "Cancelar",
                confirmButtonColor: "#007bff",
                cancelButtonColor: "#6c757d"
            }).then(async (result) => {
                if(result.isConfirmed){
                    try {
                        const {status} = await movieServices.changeStatus(id)
                        if(status === 200){
                            this.$swal.fire({
                                title: `Estado de la película actualizado correctamente`, 
                                icon: "success",
                                showCancelButton: false,
                                showConfirmButton: false,
                                timer: 3000
                            }).then(() => {
                                this.getMovies()
                            })
                        }
                    } catch (error) {
                        console.log("error",error)
                    }
                }
            })
        },
        dateFormated(date){
            return moment(date).format('DD/MM/YYYY')
        },
        showOverlay(){
            this.onDrag = !this.onDrag
        },
        showLoading(){
            this.isloading = true
        },
        hideLoading(){
            this.isloading = false
        },

        onScroll() {
          const currentScrollPosition = window.pageYOffset || document.documentElement.scrollTop;
          if (Math.abs(currentScrollPosition - this.lastScrollPosition) < 60) {
            return;
          }
          this.showElement = currentScrollPosition < this.lastScrollPosition;
          this.lastScrollPosition = currentScrollPosition;
        },
    },
    mounted(){
        this.getMovies()
        document.addEventListener('scroll', this.onScroll)
    }
    
}
</script>

<style scoped>


.hover:hover{
    box-shadow: 2px -2px 58px 15px #c4bcc499;
    transition: 0.5s;
    cursor: grab;
}

.hover:active{
  cursor: grabbing;
}

.show-button-with-media-query {
    @media (max-width: 768px){
        display: none;
    }
}

#search{
    @media (max-width: 766px){
        margin-top: 10px;
        width: 100%;
        border-radius: 3px;
    }
}


.show-label{
    @media (max-width: 768px){
        display: none;
        border-radius: 100%;
    }
}

.success{
    background-color: #28a745;
    color: white; 
}
.danger{
    background-color: #dc3545;
    color: white;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.card-style{
    animation: fadeIn 0.5s ease-in-out;
    height: 240px;
}
.delete-button:hover{
    color: black;
}

.scroll-content{
  height: 80vh;
}

.header-search{
  padding-top: 10px;
  padding-bottom: 10px;
  align-items: center;
  transition: all ease 0.5s;
}

.header-search-sticky{
  background-color: rgba(0,0,0,0.1);
  padding-top: 10px;
  padding-bottom: 10px;
  align-items: center;
  position: fixed;
  top: 50px;
  z-index: 200;
  width: 82%;
  border-radius: 5px;
  animation-name: appear;
  animation-duration: 1s;
}

@keyframes appear {
  from{
    opacity: 0;
    top: 0;
  }
  to {
    opacity: 1;
    top: 50px;
  }
}

</style>