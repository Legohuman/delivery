(function (app, undefined) {
    'use strict';

    app.factory('RenderService', function () {

        if (!Detector.webgl) Detector.addGetWebGLMessage();

        var container;

        var camera, controls, scene, renderer,
            data = {},
            updatableObjects = [],
            scaleFactor = 100,
            boxColors = [0x33ff33, 0x3333ff, 0xff33ff, 0xffff33, 0x33ffff];

        return {
            start: function () {
                init();
                animate();
            },
            update: function (packingData) {
                data = packingData;
                removeUpdatableObjects();
                addUpdatableObjects();
                render();
            }
        };


        function init() {
            container = document.getElementById('container');

            camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 1000);
            camera.position.set(100, 100, 100);
            camera.rotation.z = Math.PI / 4;

            controls = new THREE.TrackballControls(camera, container);

            controls.rotateSpeed = 1.0;
            controls.zoomSpeed = 1.2;
            controls.panSpeed = 0.8;

            controls.noZoom = false;
            controls.noPan = false;

            controls.staticMoving = true;
            controls.dynamicDampingFactor = 0.3;

            controls.keys = [65, 83, 68];

            controls.addEventListener('change', render);

            // world

            scene = new THREE.Scene();
            scene.fog = new THREE.FogExp2(0xcccccc, 0.002);
            addPlane();
            addUpdatableObjects();

            // lights

            var light = new THREE.DirectionalLight(0xffffff);
            light.position.set(1, 1, 1);
            scene.add(light);

            light = new THREE.DirectionalLight(0x002288);
            light.position.set(-1, -1, -1);
            scene.add(light);

            light = new THREE.AmbientLight(0x222222);
            scene.add(light);

            //Axis helper
            var axisHelper = new THREE.AxisHelper(30);
            scene.add(axisHelper);


            // renderer

            renderer = new THREE.WebGLRenderer({antialias: false});
            renderer.setClearColor(scene.fog.color);
            renderer.setPixelRatio(window.devicePixelRatio);
            renderer.setSize(window.innerWidth, window.innerHeight);

            container.appendChild(renderer.domElement);

            //

            window.addEventListener('resize', onWindowResize, false);
            //

            render();

        }

        function addPlane() {
            var texture = new THREE.TextureLoader().load("img/ground1.jpg");
            texture.wrapS = THREE.RepeatWrapping;
            texture.wrapT = THREE.RepeatWrapping;
            texture.repeat.set(25, 25);
            var geometry = new THREE.RingGeometry(0, 500, 16, 1);
            var material = new THREE.MeshBasicMaterial({
                color: 0x8e8e8e,
                map: texture,
                side: THREE.DoubleSide
            });
            var mesh = new THREE.Mesh(geometry, material);
            mesh.rotation.x = -Math.PI / 2;
            mesh.position.y = -0.5;
            scene.add(mesh);
        }

        function addUpdatableObjects() {
            if (data && data.contanerSize && data.placementData) {
                for (var ci = 0; ci < data.placementData.length; ci++) {
                    var widthOffset = ci * data.contanerSize[0] / scaleFactor * 1.3;
                    addBox([0, 0, 0], data.contanerSize, 0xff3333, true, widthOffset);

                    for (var bi = 0; bi < data.placementData[ci].length; bi++) {
                        addBox(data.placementData[ci][bi].start, data.placementData[ci][bi].end, boxColors[bi % boxColors.length], false, widthOffset);
                    }
                }
            }
        }

        function addBox(starts, ends, color, flipFaces, offset) {
            var width = (ends[0] - starts[0]) / scaleFactor;
            var height = (ends[1] - starts[1]) / scaleFactor;
            var depth = (ends[2] - starts[2]) / scaleFactor;
            var geometry = new THREE.BoxGeometry(width, height, depth);
            var material = new THREE.MeshPhongMaterial({color: color, shading: THREE.SmoothShading});
            if (flipFaces) {
                geometry.scale(-1, -1, -1);
            }

            var mesh = new THREE.Mesh(geometry, material);
            var x = starts[0] / scaleFactor + geometry.parameters.width / 2 + offset;
            var y = starts[1] / scaleFactor + geometry.parameters.height / 2;
            var z = starts[2] / scaleFactor + geometry.parameters.depth / 2;
            mesh.position.set(x, y, z);
            scene.add(mesh);
            updatableObjects.push(mesh);
        }

        function removeUpdatableObjects() {
            updatableObjects.forEach(function (obj) {
                scene.remove(obj);
            });
            updatableObjects = [];
        }

        function onWindowResize() {

            camera.aspect = window.innerWidth / window.innerHeight;
            camera.updateProjectionMatrix();

            renderer.setSize(window.innerWidth, window.innerHeight);

            controls.handleResize();

            render();

        }

        function animate() {

            requestAnimationFrame(animate);
            controls.update();

        }

        function render() {

            renderer.render(scene, camera);

        }

    });
})(_deliveryApp);

