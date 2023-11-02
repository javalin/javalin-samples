<template id="hello-world">
    <app-frame>
        <h1 class="hello-world">Hello, World!</h1>
        <a href="/users/">View user overview</a>
    </app-frame>
</template>
<script>
app.component("hello-world", {template: "#hello-world"});
</script>
<style>
.hello-world {
    color: goldenrod;
}
</style>
