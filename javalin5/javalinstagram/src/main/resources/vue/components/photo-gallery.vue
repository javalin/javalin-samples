<template id="photo-gallery">
  <div class="photo-gallery">
    <div class="photos">
      <p v-if="photos.loading">Loading photos ...</p>
      <p v-if="photos.loadError">Failed to load photos :/</p>
      <p v-if="photos.loaded && photos.data.length === 0">
        There are no photos on Javalinstagram yet. You should
        <a href="/my-photos">upload something</a>.
      </p>
      <template v-if="photos.loaded">
        <div v-for="photo in photos.data" class="photo" :key="photo.id">
          <img @click="openLightbox(photo.id)" :src="'/static/p/' + photo.id" />
          <div class="meta">
            <div>{{ formatDate(photo.created) }}</div>
            <div>
              {{ photo.likes }} {{ photo.likes === 1 ? "like" : "likes" }}
            </div>
            <div
              v-if="photo.isLiked"
              class="like-btn"
              @click="unlikePhoto(photo.id)"
            >
              <v-icon color="white" size="42">favorite</v-icon>
            </div>
            <div
              v-if="!photo.isLiked"
              class="like-btn"
              @click="likePhoto(photo.id)"
            >
              <v-icon color="white" size="42">favorite_border</v-icon>
            </div>
          </div>
        </div>
      </template>
    </div>
    <v-dialog
      hide-overlay
      v-model="lightbox"
      max-width="800"
      @input="(v) => v || closeLightbox()"
    >
      <v-card class="photo-gallery">
        <img
          v-if="lightboxedPhoto !== null"
          :src="`/static/p/${lightboxedPhoto}`"
        />
      </v-card>
    </v-dialog>
  </div>
</template>
<script nonce="@addNonce">
Vue.component("photo-gallery", {
  template: "#photo-gallery",
  props: ["forUser", "take"],
  data: () => ({
    lightbox: false,
    lightboxedPhoto: null,
    photos: null,
  }),
  methods: {
    openLightbox(photoId) {
      this.lightboxedPhoto = photoId;
      this.lightbox = true;
    },
    closeLightbox() {
      this.lightbox = false;
      setTimeout(() => (this.lightboxedPhoto = null), 300);
    },
    formatDate(timeInMillis) {
      return moment(timeInMillis).format("Do of MMMM, YYYY");
    },
    likePhoto(photoId) {
      axios
        .post("/api/likes?photo-id=" + photoId)
        .then(() => this.loadPhotos());
    },
    unlikePhoto(photoId) {
      axios
        .delete("/api/likes?photo-id=" + photoId)
        .then(() => this.loadPhotos());
    },
    loadPhotos() {
      if (this.photos !== null) {
        this.photos.refresh();
      }
      let owner = this.forUser
        ? "owner-id=" + this.$javalin.state.currentUser
        : "";
      let take = this.take ? "take=" + this.take : "";
      this.photos = new LoadableData("/api/photos?" + owner + "&" + take);
    },
  },
  mounted() {
    this.$eventBus.$on("photo-uploaded", () => {
      this.loadPhotos();
    });
  },
  created() {
    this.loadPhotos();
  },
});
</script>
<style>
.photo-gallery img {
  display: block;
  max-width: 100%;
}

.photo-gallery h1 {
  font-family: "Lobster Two", cursive;
  font-weight: 400;
  color: #444;
  font-size: 28px;
}

.photo-gallery .photos {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  animation: fadeIn 0.2s forwards;
}

.photo-gallery .photo {
  position: relative;
  display: block;
  width: 23%;
  margin-bottom: 24px;
  float: left;
  border-radius: 3px;
  border: 2px solid #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.4), 0 0 2px rgba(0, 0, 0, 0.2);
  background: #fff;
}

.photo-gallery .photo .meta {
  font-size: 13px;
  padding: 5px;
  height: 42px;
  overflow: hidden;
}

.photo-gallery .like-btn {
  cursor: pointer;
  position: absolute;
  right: 8px;
  bottom: 48px;
  text-shadow: 2px 2px #bc50a4, -1px -1px 2px rgba(0, 0, 0, 0.3);
  user-select: none;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
