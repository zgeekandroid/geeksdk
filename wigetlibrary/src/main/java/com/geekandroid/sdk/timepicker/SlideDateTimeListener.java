/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.geekandroid.sdk.timepicker;

import java.util.Date;

/**
 * <p>This listener class informs the client when the user sets
 * a date and time by pressing "OK", or cancels the dialog.</p>
 *
 * <p>Overriding {@code onDateTimeCancel()} is optional. The client
 * can override this to listen for when the user cancels the dialog.
 * This is called when the user presses the dialog's Cancel button,
 * touches outside the dialog or presses the device's Back button.</p>
 *
 * @author jjobes
 *
 */
public abstract class SlideDateTimeListener
{
    /**
     * Informs the client when the user presses "OK"
     * and selects a date and time.
     *
     * @param date  The {@code Date} object that contains the date
     *              and time that the user has selected.
     */
    public abstract void onDateTimeSet(Date date);

    /**
     * Informs the client when the user cancels the
     * dialog by pressing Cancel, touching outside
     * the dialog or pressing the Back button.
     * This override is optional.
     */
    public void onDateTimeCancel()
    {

    }
}
