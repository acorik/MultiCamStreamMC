# MultiCamStreamMC

Herramientas de realización y cámaras para Minecraft.

Este repositorio reúne tres componentes: **MultiStreamCamMC** para Fabric 1.20.1, **CameraOperator** para Spigot/Paper y **CameraAnimations** para animaciones de cámara.

## CameraOperator

Permite convertir jugadores en operadores y cámaras, guardar encuadres, organizar tomas en proyectos de 9 slots, crear sliders y ejecutar control LIVE con modos handheld, estabilizada y cinematica.

Comandos principales:

```text
/cam add <nombre> [slot]
/cam remove <nombre>
/cam list
/cam operator
/cam exit
/cam camera <jugador|clear>
/cam slider start <nombre>
/cam slider end <nombre> <segundos> [slot]
/cam play <nombre>
/cam test <on|off>
/cam live on <handheld|estabilizada|cinematica>
/cam live off
/cam project <create|select|delete|list> [nombre]
```

## CameraAnimations

Construye proyectos con un punto inicial y segmentos de movimiento. Cada segmento tiene duración y easing lineal, suave o cinematica.

```text
/anim project create <nombre>
/anim project select <nombre>
/anim start
/anim end <segundos> [lineal|suave|cinematica]
/anim undo
/anim clear
/anim info
/anim list
/anim play
/anim stop
```

## MultiStreamCamMC

El PR #1 añade el mod Fabric para cámaras persistentes:

```text
/multistreamcam crear <nombre>
/multistreamcam crear_en <nombre> <dimension>
/multistreamcam ir <nombre>
/multistreamcam eliminar <nombre>
/multistreamcam listar
```

En cliente, la tecla **K** solicita las cámaras y abre el menú rápido. También existe detección básica de obs-websocket en `127.0.0.1:4455`.

El mod no crea una webcam virtual del sistema operativo: para ese flujo se necesita OBS Virtual Camera u otra herramienta externa.

## Versiones recibidas

CameraOperator: 1.2.1, 2.0.0, 2.1.0 y 2.1.1.
CameraAnimations: 1.0.0.

Los hashes y metadatos están en `artifacts/VERSIONS.md`.

## Sobre el código fuente

Cuando el único artefacto disponible es un JAR, el JAR contiene bytecode, no el fuente original. La reconstrucción se documenta como tal y no se presenta como código fuente original byte-a-byte.

