package com.mercadolivro.purchase

import org.springframework.context.ApplicationEvent

class PurchaseEvent(source: Any, val purchaseModel: PurchaseModel) : ApplicationEvent(source)