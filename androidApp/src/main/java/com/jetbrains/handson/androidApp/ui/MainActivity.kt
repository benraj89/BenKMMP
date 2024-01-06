package com.jetbrains.handson.androidApp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetbrains.handson.androidApp.utils.MyApplicationTheme
import com.jetbrains.handson.androidApp.utils.Strings
import com.jetbrains.handson.kmm.shared.Repository
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import com.jetbrains.handson.kmm.shared.entity.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private var data = mutableStateOf(listOf<Event>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UI()
        }

        init()
    }

    private fun init() {
        getEvents()
    }

    private fun getEvents() {
        val repository = Repository(DatabaseDriverFactory(applicationContext))

        CoroutineScope(Dispatchers.Main).launch {
            kotlin.runCatching {
                repository.getEvents()
            }.onSuccess {
                data.value = it
            }.onFailure {
                Toast.makeText(applicationContext, "No Data Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Preview
    @Composable
    private fun UI() {
        MyApplicationTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                ScaffoldView()
            }
        }
    }

    @Composable
    private fun ScaffoldView()
    {
        Scaffold(
            topBar = {
                TopAppBar(backgroundColor = Color.Blue) {
                    Text(text = "Events", style = TextStyle(color = Color.White))
                }
            }
        ) { innerPadding -> // padding calculated by scaffold
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding) // padding applied here
            ) {
               Body()
            }
        }
    }


    @Composable
    private fun Body() {
        Column (modifier = Modifier.padding(20.dp)) {
            Text(
                text = Strings.Events,
                modifier = Modifier.fillMaxWidth(1f)
            )
            Spacer(modifier = Modifier.padding(2.dp))
            LazyColumn {
                itemsIndexed(data.value) { _: Int, item: Event ->
                    ListItem(event = item)
                }
            }
        }

    }

    @Composable
    private fun ListItem(event: Event) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .background(color = Color.DarkGray)
                .border(1.dp, Color.DarkGray)
                .padding(10.dp),
            ) {
            HorizontalTextView(header = "Event", value = event.event)
            HorizontalTextView(header = "Event Desc", value = event.event_desc)
        }

    }

    @Composable
    fun HorizontalTextView(header:String,value:String)
    {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = header, style = TextStyle(color = Color.Black))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = value, style = TextStyle(color = Color.White, fontSize = 20.sp))
        }
    }

}