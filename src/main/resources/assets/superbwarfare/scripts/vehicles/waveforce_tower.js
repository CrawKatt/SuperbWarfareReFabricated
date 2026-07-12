function transformCustomModelPart(vehicle, model, poseStack, entityYaw, partialTicks, renderer) {
    const JsMath = this.JsMath

    const glow = model.getBone("glow")
    const scale = Math.min(JsMath.lerp(partialTicks, vehicle.laserScaleO, vehicle.laserScale), 1.2)

    glow.xScale = scale
    glow.yScale = scale
    glow.zScale = scale

    const glow2 = model.getBone("glow2")

    glow2.z += -16 * vehicle.laserLength

    glow2.xScale = scale
    glow2.yScale = scale
    glow2.zScale = scale

    let charge = model.getBone("charge")
    let energy = vehicle.chargeProgress
    let energyRate0 = renderer.getEnergy0()
    charge.zScale = JsMath.lerp(partialTicks, energyRate0, energy)
    renderer.setEnergy0(energy)

    for (let i = 1; i <= 7; i++) {
        let boneName = `light_on${i}`
        let bone = model.getBone(boneName)

        if (bone != null) {
            bone.visible = energy >= (i / 7.0)
        }
    }
}