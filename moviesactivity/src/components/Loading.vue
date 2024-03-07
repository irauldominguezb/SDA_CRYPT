<template>
    <div class="loading">
        <b-spinner></b-spinner>
    </div>
</template>

<script>
export default {
name: 'Loading-Component'
}
</script>

<style scoped>
.loading {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 20vh;
    width: 100%;
    background-color: transparent;
    position: absolute;
    margin-bottom: 30px;
    color:blue;
    border-radius: 5px;
}

</style>