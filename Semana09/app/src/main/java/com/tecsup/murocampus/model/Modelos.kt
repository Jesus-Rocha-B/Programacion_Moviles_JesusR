package com.tecsup.murocampus.model

// Ejemplo resuelto [E]: Usuario
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

// [G] GUIADO: Post, Comentario, NuevoPost
// TODO (Paso 2): Definir @Serializable data class Post(userId, id, title, body)
// TODO (Paso 2): Definir @Serializable data class Comentario(postId, id, name, email, body)
// TODO (Paso 2): Definir @Serializable data class NuevoPost(userId, title, body)
