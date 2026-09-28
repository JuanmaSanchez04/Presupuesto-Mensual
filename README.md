# Presupuesto Mensual

App Android para llevar el control de tus ingresos y gastos mes a mes. Registras movimientos, los organizas por categorías y ves de un vistazo cuánto has ingresado, cuánto has gastado y cuál es tu balance.

Todos los datos se guardan en el propio móvil, sin cuentas ni conexión a internet.

## Funcionalidades

- **Resumen mensual:** ingresos, gastos y balance del mes, con flechas para moverte entre meses.
- **Movimientos:** crear, editar y borrar, con importe, fecha, descripción y categoría. El importe se muestra en verde o rojo según sea ingreso o gasto.
- **Categorías:** crear, editar y borrar. Cada categoría es de tipo ingreso o gasto, y ese tipo se aplica automáticamente a los movimientos que la usan.
- **Buscador de categorías** que filtra la lista mientras escribes.
- **Validación de formularios:** los campos obligatorios se marcan y se avisa si falta alguno antes de guardar.
- **Confirmación antes de borrar,** para evitar eliminar datos por error.
- **Coherencia de datos:** si cambias el tipo de una categoría, sus movimientos se actualizan con ella. Al borrar una categoría se eliminan también sus movimientos.

## Tecnologías

- **Java 11** y **Android Studio**
- **SQLite** con `SQLiteOpenHelper`, sin librerías de persistencia externas
- **Fragments** con `FragmentManager` y `BottomNavigationView` para la navegación
- **RecyclerView** con adaptadores propios
- **Material Components** (tarjetas, campos de texto, botón flotante, diálogos)

## Estructura del proyecto

```
app/src/main/java/com/example/prueba/
├── MainActivity.java        # Barra de navegación inferior y cambio de pantallas
├── Fragment/                # Pantallas: resumen, categorías y sus formularios
├── Adapter/                 # Adaptadores de las listas (RecyclerView)
├── DAO/                     # Acceso a datos: consultas y operaciones sobre SQLite
├── Database/                # DBHelper: creación de las tablas
└── clases/                  # Modelos: Categoria y Movimiento
```

## Modelo de datos

Dos tablas relacionadas por clave foránea:

| categorias | movimientos |
|---|---|
| `id` (PK, autoincremental) | `id` (PK, autoincremental) |
| `nombre` | `importe` |
| `tipo` (`INGRESO` / `GASTO`) | `fecha` (milisegundos) |
| `color` | `descripcion` |
| | `tipo` (`INGRESO` / `GASTO`) |
| | `categoriaId` (FK → `categorias.id`, `ON DELETE CASCADE`) |

## Cómo ejecutarlo

1. Clona el repositorio:
   ```bash
   git clone https://github.com/TU_USUARIO/Presupuesto-Mensual.git
   ```
2. Ábrelo con una versión reciente de **Android Studio** (el proyecto usa el Android Gradle Plugin 9.3.3).
3. Espera a que Gradle sincronice las dependencias.
4. Ejecuta la app en un emulador o en un móvil con Android 7.0 (API 24) o superior.

Para probar la app desde cero, crea primero al menos una categoría de ingreso y una de gasto, y después añade movimientos desde la pantalla de resumen.

## Decisiones técnicas

- **SQLite directo en vez de Room:** quería controlar las consultas SQL y entender qué pasa por debajo. Cada DAO es una clase normal con métodos `create`, `update` y `remove`, sin anotaciones.
- **El tipo del movimiento lo decide la categoría:** el usuario solo elige la categoría, no hay que indicar dos veces si es ingreso o gasto. Ese tipo se guarda también en cada movimiento para que los totales mensuales se calculen con una única consulta, y se sincroniza al editar la categoría.
- **Navegación con `FragmentManager`:** para tres pantallas principales y dos formularios, una barra inferior más transacciones de fragments es suficiente y evita dependencias extra.

## Posibles mejoras

- Gastos fijos recurrentes que se registren solos cada mes.
- Límite mensual por categoría con aviso al acercarte.
- Gráficas y comparativa entre meses.
- Enfoque para autónomos: separar IVA e IRPF.
- Exportar los datos a CSV.

## Autor

Desarrollado por Juanma como proyecto de portfolio.
