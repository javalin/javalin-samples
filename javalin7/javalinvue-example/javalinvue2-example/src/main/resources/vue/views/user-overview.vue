<template id="user-overview">
    <app-frame>
        <div v-if="users.loading">Loading users...</div>
        <div v-if="users.loadError">Error while loading users</div>
        <ul v-if="users.loaded" class="user-overview-list">
            <li v-for="user in users.data">
                <a :href="`/users/${user.id}`">{{ user.name }} ({{ user.email }})</a>
            </li>
        </ul>
    </app-frame>
</template>
<script>
Vue.component("user-overview", {
    template: "#user-overview",
    data: () => ({
        users: new LoadableData("/api/users"),
    }),
});
</script>
<style>
ul.user-overview-list {
    padding: 0;
    list-style: none;
}

ul.user-overview-list a {
    display: block;
    padding: 16px;
    border-bottom: 1px solid #ddd;
}

ul.user-overview-list a:hover {
    background: #00000010;
}
</style>
