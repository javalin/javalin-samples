<template id="app-frame">
    <div class="app-frame">
        <header>
            <span>JavalinVue demo app</span>
            <span v-if="$javalin.state.currentUser">Current user: '{{$javalin.state.currentUser}}'</span>
        </header>
        <slot></slot>
    </div>
</template>
<script>
    app.component("app-frame", {template: "#app-frame"});
</script>
<style>
    html {
        font-family: sans-serif;
    }
    .app-frame > header {
        padding: 20px;
        background: #b6e2ff;
        font-size: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
</style>
