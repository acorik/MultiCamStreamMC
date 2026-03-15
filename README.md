# MultiStreamCamMC

Mod Fabric (1.20.1) para crear cámaras de streaming en el mundo y usarlas de forma rápida en singleplayer o servidores.

## Funciones incluidas

- Comando para guardar una cámara exactamente en la posición y ángulo donde miras.
- Persistencia en el mundo (las cámaras quedan guardadas por servidor/mundo).
- Menú rápido del lado cliente (`K`) para saltar de forma instantánea a una cámara.
- Compatible con servidores: los datos viven en el servidor y se sincronizan al cliente.
- Detección básica de OBS vía `obs-websocket` en `127.0.0.1:4455`.

## Comandos

> Requieren nivel de permiso 2.

- `/multistreamcam crear <nombre>`
- `/multistreamcam crear_en <nombre> <dimension>`
- `/multistreamcam ir <nombre>`
- `/multistreamcam eliminar <nombre>`
- `/multistreamcam listar`

## Integración con Stream Deck

Opciones recomendadas:

1. **Servidor dedicado**: usar botones de Stream Deck para disparar comandos RCON (`/multistreamcam ir cam1`).
2. **Cliente local**: abrir menú rápido con `K` y seleccionar cámara.

## Nota sobre “cámara en el PC detectable por OBS”

Minecraft/Fabric no puede crear por sí solo un dispositivo virtual de cámara del sistema operativo. Para ese flujo usa una herramienta externa de cámara virtual (OBS Virtual Camera u otra), y este mod se encargará de posicionar/saltar cámaras dentro del juego.
