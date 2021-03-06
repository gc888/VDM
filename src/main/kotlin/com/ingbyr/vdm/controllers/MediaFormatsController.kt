package com.ingbyr.vdm.controllers

import com.ingbyr.vdm.engine.AbstractEngine
import com.ingbyr.vdm.engine.utils.EngineFactory
import com.ingbyr.vdm.engine.utils.EngineType
import com.ingbyr.vdm.events.StopBackgroundTask
import com.ingbyr.vdm.utils.MediaFormat
import com.ingbyr.vdm.utils.VDMProxy
import org.slf4j.LoggerFactory
import tornadofx.*
import java.util.*

class MediaFormatsController : Controller() {

    private val logger = LoggerFactory.getLogger(MediaFormatsController::class.java)
    var engine: AbstractEngine? = null

    init {
        messages = ResourceBundle.getBundle("i18n/MediaFormatsView")
        subscribe<StopBackgroundTask> {
            engine?.stopTask()
        }
    }


    fun requestMedia(engineType: EngineType, url: String, proxy: VDMProxy): List<MediaFormat>? {
        engine = EngineFactory.create(engineType)
        if (engine != null) {
            engine!!.url(url).addProxy(proxy)
            try {
                val jsonData = engine!!.fetchMediaJson()
                return engine!!.parseFormatsJson(jsonData)
            } catch (e: Exception) {
                logger.error(e.toString())
            }
        } else {
            logger.error("bad engine: $engineType")
        }
        return null
    }

    fun clear() {
        engine = null
    }
}