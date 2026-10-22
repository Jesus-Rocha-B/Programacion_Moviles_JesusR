# HuellitasVet

HuellitasVet es una aplicación de gestión de una veterinaria que admite dos perfiles en el mismo dispositivo. El Dueño registra mascotas, pide citas eligiendo servicio, fecha y hora, y consulta el historial clínico. El Veterinario ve la agenda, confirma citas, registra la atención (diagnóstico, tratamiento y peso), consulta pacientes y administra servicios. Ambos perfiles comparten la misma base de datos Room.

Tecnologías utilizadas: Kotlin, Jetpack Compose, Material 3, Room, DataStore, Navigation Compose, MVVM + Repository.

Datos de prueba para ingresar:
- Dueño: `ana@correo.test` / `1234` y `luis@correo.test` / `1234`
- Veterinario: `vet@huellitas.test` / `admin`

## 1. ÁRBOL DE PAQUETES

```text
com.rocha.huellitasvet/
│
├── MainActivity.kt                  # Actividad principal que establece la vista y el tema
├── VetApp.kt                        # Composable raíz que inicializa la base de datos, el repositorio y DataStore
│
├── data/                            # Capa de acceso y gestión de datos
│   ├── local/                       # Base de datos local Room
│   │   ├── dao/                     # Interfaces DAO para consultas e interacciones con las tablas
│   │   ├── entity/                  # Entidades que representan las tablas de la base de datos
│   │   ├── relation/                # Clases de relación entre entidades (1 a N, N a M)
│   │   ├── DatosIniciales.kt        # Población inicial de datos de prueba en Room
│   │   └── VetDatabase.kt           # Definición de la base de datos Room huellitasvet.db
│   ├── prefs/                       # Gestión de preferencias del usuario con DataStore
│   └── repository/                  # Abstracción e implementación de fuentes de datos (Repository)
│
├── model/                           # Modelos de dominio, enumeraciones y funciones de extensión
│
├── ui/                              # Capa de interfaz de usuario construida en Jetpack Compose
│   ├── components/                  # Componentes de interfaz reutilizables
│   │   └── graficos/                # Componentes para renderizado de gráficos y métricas
│   ├── dueno/                       # Pantallas del perfil de usuario Dueño
│   ├── navigation/                  # Grafo de navegación y definición de rutas de la app
│   ├── theme/                       # Configuración de tema, colores y tipografía con Material 3
│   └── veterinario/                 # Pantallas del perfil de usuario Veterinario
│
└── viewmodel/                       # ViewModels para gestión del estado de la UI y lógica de presentación
```

## 2. DIAGRAMA MVVM

```text
View (Composable) -> ViewModel (StateFlow + reglas) -> VetRepository (interface) -> VetRepositoryRoom -> DAO -> SQLite (huellitasvet.db), y DataStore (solo preferencias) conectado al ViewModel.
```

| Capa | Qué conoce | Qué NO debe hacer |
| --- | --- | --- |
| View | Solo conoce su ViewModel | No usa DAO, Repository ni contiene reglas de negocio |
| ViewModel | Solo conoce la interfaz VetRepository | No conoce Composables ni referencias directa a DAOs |
| Repository | Conoce los DAOs | No gestiona la UI ni maneja directamente Composables |
| DAO | Conoce SQL y entidades | No conoce el Repository ni componentes de la UI |

Regla de oro: lectura que debe actualizar la pantalla -> Flow (sin suspend); escritura o acción única -> suspend.

## 3. CAPTURAS DE PANTALLA

<img src="IMG huellitas/WhatsApp Image 2026-10-21 at 20.32.40.jpeg" width="300" />
<img src="IMG huellitas/WhatsApp Image 2026-10-21 at 20.32.40 (1).jpeg" width="300" />
<img src="IMG huellitas/WhatsApp Image 2026-10-21 at 20.32.40 (2).jpeg" width="300" />
<img src="IMG huellitas/WhatsApp Image 2026-10-21 at 20.32.41.jpeg" width="300" />
<img src="IMG huellitas/WhatsApp Image 2026-10-21 at 20.32.41 (1).jpeg" width="300" />
<img src="IMG huellitas/WhatsApp Image 2026-10-21 at 20.32.44.jpeg" width="300" />
<img src="IMG huellitas/0fbf4d32-0e43-4d62-b102-c035db0cf935.jpg" width="300" />
<img src="IMG huellitas/158465b9-1953-4738-9064-95416b8b58da.jpg" width="300" />
<img src="IMG huellitas/2582c706-7c5d-4207-b7da-c3f3607c4e53.jpg" width="300" />
<img src="IMG huellitas/3280a2a4-7f2f-4e06-90d6-528369788151.jpg" width="300" />
<img src="IMG huellitas/3280a2a4-7f2f-4e06-90d6-528369788151 (1).jpg" width="300" />
<img src="IMG huellitas/36c16e97-426a-4c01-aa3b-268bcd0248cf.jpg" width="300" />
<img src="IMG huellitas/38c7b0e1-10f3-4402-b2e6-d2414d7ed9cd.jpg" width="300" />
<img src="IMG huellitas/49290fcc-4cb0-4263-b4a1-b6340482376a.jpg" width="300" />
<img src="IMG huellitas/60aac72e-ae6c-4817-8567-7e2dc28cf8c9.jpg" width="300" />
<img src="IMG huellitas/7e69e45d-885c-41e2-86c3-885045665120.jpg" width="300" />
<img src="IMG huellitas/a9646c9b-9907-4541-83bb-8deda8f66e59.jpg" width="300" />
<img src="IMG huellitas/beff06ce-eaa5-4277-85e0-34f500b4377c.jpg" width="300" />
<img src="IMG huellitas/c31fec75-3353-4b8f-a9af-ac90784766d0.jpg" width="300" />
<img src="IMG huellitas/e3a1f74e-d4ea-493d-9ae4-9d16db214da6.jpg" width="300" />
<img src="IMG huellitas/ea9c8e81-39c4-45bc-98c3-04e5ec677724.jpg" width="300" />
<img src="IMG huellitas/f5bdf89a-e5c1-4ee2-ad2a-557e3b5c5394.jpg" width="300" />
<img src="IMG huellitas/fba204aa-c1c0-4b7f-8e4b-4e1f1ce791c8.jpg" width="300" />
