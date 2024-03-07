<template>
  <b-modal id="modal-save-movie" no-close-on-backdrop centered no-close-on-esc size="md">
    <template v-slot:modal-header="{}">
        <h5>Nueva película</h5>
    </template>
    <b-row>
        <b-col cols="12" class="mb-1">
            <b-form-group
                id="fieldset-movie-name"
                label-for="input-movie-title"
                invalid-feedback="Debes ingresar un título válido."
            >
                <label class="mandatory-field">Título: </label>
                <b-form-input
                    v-model="v$.movie.name.$model"
                    :state="v$.movie.name.$dirty ? !v$.movie.name.$error : null"
                    type="text"
                    label="input-movie-title"
                >
                </b-form-input>
            </b-form-group>
        </b-col>
    </b-row>
    <b-row>
        <b-col cols="12" md="6" lg="6" class="mb-1">
            <b-form-group
                id="fieldset-movie-director"
                label-for="input-movie-director"
                invalid-feedback="Debes ingresar un nombre director válido."
            >
                <label class="mandatory-field">Director: </label>
                <b-form-input
                    type="text"
                    v-model="v$.movie.director.$model"
                    :state="v$.movie.director.$dirty ? !v$.movie.director.$error : null"
                    label="input-movie-director"
                >
                </b-form-input>
            </b-form-group>
        </b-col>
        <b-col cols="12" md="6" lg="6" class="mb-1">
            <b-form-group
                id="fieldset-movie-duration"
                label-for="input-movie-duration"
                invalid-feedback="Asegurate que la duración sea válida. Ejemplo: 2h 30m"
            >
                <label class="mandatory-field">Duración: </label>
                <b-form-input
                    type="text"
                    v-model="v$.movie.duration.$model"
                    :state="v$.movie.duration.$dirty ? !v$.movie.duration.$error : null"
                    label="input-movie-duration"
                >
                </b-form-input>
            </b-form-group>
        </b-col>
    </b-row>
    <b-row>
        <b-col cols="12" class="mb-1">
            <b-form-group
                id="fieldset-movie-genre"
                label-for="input-movie-genre"
                invalid-feedback="Debes seleccionar un género."
            >
                <label class="mandatory-field">Genero: </label>
                <multiselect
                    v-model="v$.selected.$model"
                    :state="v$.selected.$dirty ? !v$.selected.$error : null"
                    :options="genres"
                    placeholder="Selecciona un género"
                    :allow-empty="false"
                    track-by="id"
                    label="name"
                    :close-on-select="true"
                >
                </multiselect>
            </b-form-group>
        </b-col>
        <b-col cols="12" class="mb-1">
            <b-form-group
                id="fieldset-movie-publication"
                label-for="input-movie-publication"
                invalid-feedback="Por favor selecciona una fecha de lanzamiento válida."
            >
                <label class="mandatory-field">Lanzamiento: </label>
                <b-form-input
                    class="text-secondary"
                    type="date"
                    v-model="v$.movie.publication.$model"
                    :state="v$.movie.publication.$dirty ? !v$.movie.publication.$error : null"
                    label="input-movie-duration"
                    :max="getDate()"
                >
                </b-form-input>
            </b-form-group>
        </b-col>
    </b-row>
    <template slot="modal-footer">
        <b-row>
            <b-col 
                cols="12" 
                class="justify-content-center"
            >
                <b-button 
                    @click="closeModalSave()" 
                    class="m-1"
                >
                    Cancelar
                </b-button>
                <b-button 
                    @click="saveMovie()" 
                    variant="primary"
                    :disabled="v$.movie.name.$invalid || v$.movie.director.$invalid || v$.movie.duration.$invalid || v$.selected.$invalid || v$.movie.publication.$invalid"
                >
                    Registrar
                </b-button>
            </b-col>
        </b-row>
    </template>
  </b-modal>
</template>

<script>

import genreServices from "@/services/Genre"
import movieServices from "@/services/Movie" 
import { useVuelidate } from '@vuelidate/core'
import { required, minLength } from '@vuelidate/validators'
import {decrypt, encrypt} from '@/config/utils'
export default {
    name: 'SaveMovie',
    setup(){
        return { v$: useVuelidate() }
    },data() {
        return {
            movie: {
                name: "",
                director: "",
                duration: "",
                gender: {
                    id: 0
                },
                publication: ""
            },
            genres: [],
            selected: null,
            
        };
    },
    methods: {
        saveMovie() {
            this.$swal.fire({
                title: "¿Estás seguro que deseas registrar esta pelicula?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Aceptar",
                cancelButtonText: "Cancelar",
                confirmButtonColor: "#007bff",
                cancelButtonColor: "#6c757d",
            }).then(async (result) => {
                if(result.isConfirmed){
                    try {
                        const {id} = this.selected;
                        this.movie.gender = id;
                        // Convertir el objeto 'movie' a formato JSON y encriptar el resultado
                        const objectToEncrypt = JSON.stringify(this.movie)
                        const chiperedObject = await encrypt(objectToEncrypt)
                        // Desencriptar el objeto cifrado y mostrar el resultado en la consola
                        const unzipObject = await decrypt(chiperedObject)
                        console.log("=>",unzipObject)
                         // Intentar guardar la película en el servicio 'movieServices' con la data cifrada
                        const {status} = await movieServices.saveMovie(chiperedObject)
                        if(status === 200){
                            this.$swal.fire({
                                title: "¡Registro exitoso!",
                                icon: "success",
                                showCancelButton: false,
                                showConfirmButton: false,
                                timer: 3000
                            });
                            this.$emit("getMovies");
                            this.closeModalSave();
                        } 
                    } catch (error) {
                        console.error(error);
                    }
                }
            })
        },
        closeModalSave(){
            this.$bvModal.hide("modal-save-movie");
            this.v$.$reset()
            this.movie = {
                title: "",
                director: "",
                duration: "",
                genre: "",
                publication: ""
            }
        },
        async getGenres(){
            try {
                const {data, status} = await genreServices.getGeneres();
                if(status === 200){
                    this.genres = data;
                }
            } catch (error) {
                console.error(error);
            }
        },
        getDate(){
            return new Date().toISOString().split('T')[0];
        }

    },
    validations: {
        movie: {
            name: { 
                required,
                minLength: minLength(3),
                valid: (value) => {
                    const format = /^([A-Za-zÑñáéíóúÁÉÍÓÚ]+[\s]*)+$/.test(value);
                    return format;
                }
            },
            director: { 
                required,
                valid: (value) => {
                    const format = /^([A-Za-zÑñáéíóúÁÉÍÓÚ]+[\s]*)+$/.test(value);
                    return format;
                }
            },
            duration: { 
                required,
                valid:(value) => {
                    const format = /^\d+h \d+m$/.test(value.toString());
                    return format;
                }
            },
            publication: {
                required
            }
        },
        selected: {
            required
        }
    },
    mounted(){
        this.getGenres()
    }
}
</script>

<style scoped>
.mandatory-field::after{
    content: " * ";
    color: red
}


.custom-select-success {
    border-color: #28a745;
}
</style>