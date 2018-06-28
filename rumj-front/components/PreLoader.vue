<template>
    <semipolar-spinner v-if="spin" class="center_loading"
                       :animation-duration="2000"
                       :size="80"
                       :color="'#45baff'"
    />
</template>

<script>
    export default {
        name: "preLoader",
        props: {
            spin: Boolean
        }
    }
</script>

<style scoped>

</style>
