<template>
  <b-card>
    <b-row>
        <b-col>
            <b-form-group
                id="fieldset-search"
                label-for="input-search"
          >
            <b-form-input
                id="search"
                pill
                :placeholder="'Buscar...'"
                class="sticky-top"
            >
            </b-form-input>

          </b-form-group>
        </b-col>
    </b-row>
  </b-card>
</template>

<script>
export default {
    name: 'SearchComponent',
}
</script>

<style scoped>
.sticky-top{
    position: sticky;
    top: 0;
}
</style>